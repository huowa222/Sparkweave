/*
 * Copyright (c) 2011, University of Innsbruck, Austria.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along
 * with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package at.sti2.spark.core.triple;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class RDFTriple implements Serializable{
	
	private static final long serialVersionUID = -2301534685285465150L;

	public enum Field {SUBJECT, PREDICATE, OBJECT};
	
	private RDFValue subject;
	private RDFValue predicate;
	private RDFValue object;
	
	public RDFTriple(){
		
	}
	
	public RDFTriple(RDFValue subject, RDFValue predicate, RDFValue object){
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public RDFValue getSubject() {
		return subject;
	}
	public void setSubject(RDFValue subject) {
		this.subject = subject;
	}
	public RDFValue getPredicate() {
		return predicate;
	}
	public void setPredicate(RDFValue predicate) {
		this.predicate = predicate;
	}
	public RDFValue getObject() {
		return object;
	}
	public void setObject(RDFValue object) {
		this.object = object;
	}
	
	@Override
	public boolean equals(Object that){
		
		//reference check (fast)
		if(this == that) return true;
		//type check and null check
		if(!(that instanceof RDFTriple)) return false;
		RDFTriple rdfTriple = (RDFTriple)that;

		return subject.equals(rdfTriple.getSubject()) &&
		       predicate.equals(rdfTriple.getPredicate())&&
		       object.equals(rdfTriple.getObject());
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(19, 37)
				.append(subject)
				.append(predicate)
				.append(object)
				.toHashCode();
	}
	

	public RDFValue getValueOfField(Field field){
		
		if (field == Field.SUBJECT){
			if (subject instanceof RDFURIReference)
				return subject;
		} else if (field == Field.PREDICATE){
			return predicate;
		} else if (field == Field.OBJECT){
			return object;
		}
		
		return null;
	}
	
	public String toString(){
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(subject.toString());
		buffer.append(" ");
		buffer.append(predicate.toString());
		buffer.append(" ");
		buffer.append(object.toString());
		
		return buffer.toString();
	}
}
