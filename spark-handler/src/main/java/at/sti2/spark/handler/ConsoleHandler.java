package at.sti2.spark.handler;

import org.apache.log4j.Logger;

import at.sti2.spark.core.solution.Match;
import at.sti2.spark.core.triple.RDFLiteral;
import at.sti2.spark.core.triple.RDFURIReference;
import at.sti2.spark.core.triple.RDFValue;
import at.sti2.spark.core.triple.RDFVariable;
import at.sti2.spark.core.triple.TripleCondition;
import at.sti2.spark.grammar.pattern.Handler;

public class ConsoleHandler implements SparkweaveHandler {

	private static Logger log = Logger.getLogger(ConsoleHandler.class);
	
	private long noMatches = 0;
	
	private boolean verbose = true;
	
	Handler handlerProperties = null;
	
	@Override
	public void init(Handler handlerProperties) {
		this.handlerProperties = handlerProperties;
		
		String strVerbose = handlerProperties.getValue("verbose");
		if(strVerbose!=null){
			if(strVerbose.equals("false"))
				verbose = false;
			else
				verbose = true;
		}
	}
	
	@Override
	public void invoke(Match match) throws SparkweaveHandlerException{

		noMatches++;
		log.info("Match no " + noMatches);
		
		if(verbose){
			//Format the output for the match
			String ntriplesOutput = formatMatchNTriples(match, handlerProperties);
			log.info(ntriplesOutput);			
		}
		
	}
	
	private String formatMatchNTriples(Match match, Handler handlerProperties){
		
		StringBuffer buffer = new StringBuffer();
		for (TripleCondition condition : handlerProperties.getTriplePatternGraph().getConstructConditions()){
			
			//Resolve subject
			buffer.append('<');
			if(condition.getConditionTriple().getSubject() instanceof RDFURIReference)
				
				buffer.append(((RDFURIReference)condition.getConditionTriple().getSubject()).toString());
			
			else if (condition.getConditionTriple().getSubject() instanceof RDFVariable){
				
				String variableId = ((RDFVariable)condition.getConditionTriple().getSubject()).getVariableId();
				buffer.append(match.getVariableBindings().get(variableId).toString());
				
			}
			buffer.append("> ");
			
			//Resolve predicate
			buffer.append('<');
			if(condition.getConditionTriple().getPredicate() instanceof RDFURIReference)
				
				buffer.append(((RDFURIReference)condition.getConditionTriple().getPredicate()).toString());
			
			else if (condition.getConditionTriple().getPredicate() instanceof RDFVariable){
				
				String variableId = ((RDFVariable)condition.getConditionTriple().getPredicate()).getVariableId();
				buffer.append(match.getVariableBindings().get(variableId).toString());
				
			}
			buffer.append("> ");
			
			//Resolve object
			if(condition.getConditionTriple().getObject() instanceof RDFURIReference){
				
				buffer.append('<');
				buffer.append(((RDFURIReference)condition.getConditionTriple().getObject()).toString());
				buffer.append("> .\n");
				
			} else if (condition.getConditionTriple().getObject() instanceof RDFVariable){
				
				String variableId = ((RDFVariable)condition.getConditionTriple().getObject()).getVariableId();
				RDFValue value = match.getVariableBindings().get(variableId);
				
				if (value instanceof RDFURIReference){
					buffer.append('<');
					buffer.append(value.toString());
					buffer.append("> .\n");
				} else if (value instanceof RDFLiteral){
					buffer.append('\"');
					buffer.append(((RDFLiteral)value).getValue());
					buffer.append('\"');
					buffer.append("^^<");
					buffer.append(((RDFLiteral)value).getDatatypeURI());
					buffer.append("> .\n");
				}
			} else if (condition.getConditionTriple().getObject() instanceof RDFLiteral){
				buffer.append('\"');
				buffer.append(((RDFLiteral)condition.getConditionTriple().getObject()).getValue());
				buffer.append('\"');
				buffer.append("^^<");
				buffer.append(((RDFLiteral)condition.getConditionTriple().getObject()).getDatatypeURI());
				buffer.append("> .\n");
			}
		}
		return buffer.toString();
	}
}
