/*******************************************************************************
 * Copyright (c) 2007-2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.ImportBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

public class ImportRegistry {
	
	class TypeReferenceVisitor extends ASTVisitor {
		public boolean visit(AllocationExpression allocationExpression, BlockScope scope) {
			if (allocationExpression.binding != null) {
				ReferenceBinding allocatedType =  allocationExpression.binding.declaringClass;
				ImportRegistry.this.importJavaLangArguments =
					ImportRegistry.this.importJavaLangArguments ||
						allocatedType.id != TypeIds.T_JavaLangString &&
						allocationExpression.binding.declaringClass.id != TypeIds.T_JavaLangObject &&
						!Util.isIntrinsic(allocatedType) &&
						!Util.isSingleConstructor(allocationExpression.binding);
			}
			return super.visit(allocationExpression, scope);
		}
		public boolean visit(
				ArrayAllocationExpression arrayAllocationExpression,
				BlockScope scope) {
			ImportRegistry.this.importJavaLangJavaArray = true;
			return super.visit(arrayAllocationExpression, scope);
		}			
		public boolean visit(ArrayQualifiedTypeReference reference, BlockScope scope) {
//			 do nothing (array type --> 'Array'
//			if (reference.resolvedType != null) {
//				ImportRegistry.this.record(reference.resolvedType);
//			}
			ImportRegistry.this.importJavaLangJavaArray = true;
			return super.visit(reference, scope);
		}

		public boolean visit(ArrayQualifiedTypeReference reference, ClassScope scope) {
//			 do nothing (array type --> 'Array'
//			if (reference.resolvedType != null) {
//				ImportRegistry.this.record(reference.resolvedType);
//			}
			ImportRegistry.this.importJavaLangJavaArray = true;
			return super.visit(reference, scope);
		}

		public boolean visit(ArrayTypeReference reference, BlockScope scope) {
//			 do nothing (array type --> 'Array'
//			if (reference.resolvedType != null) {
//				ImportRegistry.this.record(reference.resolvedType);
//			}
			ImportRegistry.this.importJavaLangJavaArray = true;
			return super.visit(reference, scope);
		}

		public boolean visit(ArrayTypeReference reference, ClassScope scope) {
//			 do nothing (array type --> 'Array'
//			if (reference.resolvedType != null) {
//				ImportRegistry.this.record(reference.resolvedType);
//			}
			ImportRegistry.this.importJavaLangJavaArray = true;
			return super.visit(reference, scope);
		}

		public boolean visit(BinaryExpression binaryExpression, BlockScope scope) {
			if (binaryExpression.resolvedType.id == TypeIds.T_JavaLangString) {
				switch (binaryExpression.left.resolvedType.id) {
					case TypeIds.T_char:
					case TypeIds.T_byte:
					case TypeIds.T_short:
					case TypeIds.T_boolean:
	//				case T_void: - should not happen
					case TypeIds.T_long:
					case TypeIds.T_double:
					case TypeIds.T_float:
					case TypeIds.T_int:
					case TypeIds.T_JavaLangString:
					case TypeIds.T_null:
						break;
					default:
						if (!Util.isIntrinsic(binaryExpression.left.resolvedType) &&
								!binaryExpression.left.isThis()) {
							ImportRegistry.this.importJavaLangSystem = true;
							return super.visit(binaryExpression, scope);
						}
				}
				switch (binaryExpression.right.resolvedType.id) {
					case TypeIds.T_char:
					case TypeIds.T_byte:
					case TypeIds.T_short:
					case TypeIds.T_boolean:
	//				case T_void: - should not happen
					case TypeIds.T_long:
					case TypeIds.T_double:
					case TypeIds.T_float:
					case TypeIds.T_int:
					case TypeIds.T_JavaLangString:
					case TypeIds.T_null:
						break;
					default:
						if (!Util.isIntrinsic(binaryExpression.right.resolvedType) &&
								!binaryExpression.right.isThis()) {
							ImportRegistry.this.importJavaLangSystem = true;
							return super.visit(binaryExpression, scope);
						}
				}
			}
			return super.visit(binaryExpression, scope);
		}
		public boolean visit(CastExpression castExpression, BlockScope scope) {
			ImportRegistry.this.recordReferenceType(castExpression.resolvedType);
			return super.visit(castExpression, scope);
		}
		public boolean visit(ClassLiteralAccess classLiteral, BlockScope scope) {
			ImportRegistry.this.importJavaLangClass = true;
			return super.visit(classLiteral, scope);
		}
		
		public boolean visit(CompilationUnitDeclaration unit, CompilationUnitScope scope) {
			ImportBinding[] importBindings = scope.imports;
			for (int i = 0, length = importBindings.length; i < length; i++) {
				if (importBindings[i].resolvedImport instanceof TypeBinding) {
					ImportRegistry.this.recordImportedType((TypeBinding)importBindings[i].resolvedImport);
				}
			}
			return super.visit(unit, scope);
		}

		public boolean visit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
			ImportRegistry.this.importJavaLangArguments =
				ImportRegistry.this.importJavaLangArguments ||
					explicitConstructor.binding.declaringClass.id != TypeIds.T_JavaLangObject &&
					!Util.isSingleConstructor(explicitConstructor.binding) &&
					!Util.isIntrinsic(explicitConstructor.binding.declaringClass);
			return super.visit(explicitConstructor, scope);
		}
		
		public boolean visit(FieldReference reference, BlockScope scope) {
			FieldBinding fieldBinding = reference.fieldBinding(); 
			if (fieldBinding != null && fieldBinding.isStatic()) {
				ImportRegistry.this.recordReferenceType(reference.fieldBinding().declaringClass);
			}
			return super.visit(reference, scope);
		}

		public boolean visit(FieldReference reference, ClassScope scope) {
			if (reference.fieldBinding().isStatic()) {
				ImportRegistry.this.recordReferenceType(reference.fieldBinding().declaringClass);
			}
			return super.visit(reference, scope);
		}

		public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
			// import already handled in unit declarationt traversal
			return super.visit(importRef, scope);
		}
		
		public boolean visit(MessageSend messageSend, BlockScope scope) {
			if (messageSend.binding != null && messageSend.binding.isStatic()) {
				ImportRegistry.this.recordReferenceType(messageSend.binding.declaringClass);
			}
			return super.visit(messageSend, scope);
		}

		public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
			if (methodDeclaration.ignoreFurtherInvestigation) return false;
			if (methodDeclaration.binding == null) return false;
			return super.visit(methodDeclaration, scope);
		}

		public boolean visit(ParameterizedQualifiedTypeReference reference, BlockScope scope) {
			//	array type --> 'Array'
			if (reference.dimensions() == 0) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(ParameterizedQualifiedTypeReference reference, ClassScope scope) {
			//	array type --> 'Array'
			if (reference.dimensions() == 0) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(ParameterizedSingleTypeReference reference, BlockScope scope) {
			//	array type --> 'Array'
			if (reference.dimensions() == 0) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(ParameterizedSingleTypeReference reference, ClassScope scope) {
			//	array type --> 'Array'
			if (reference.dimensions() == 0) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}
		
		public boolean visit(QualifiedNameReference reference, BlockScope scope) {
			switch (reference.bits & ASTNode.RestrictiveFlagMASK) {
				case Binding.TYPE:
					if (reference.resolvedType != null) {
						ImportRegistry.this.recordReferenceType(reference.resolvedType);
					}
					break;
				case Binding.FIELD:
					ImportRegistry.this.recordReferenceType(reference.actualReceiverType);
					break;
			}
			return super.visit(reference, scope);
		}

		public boolean visit(QualifiedNameReference reference, ClassScope scope) {
			if ((reference.bits & ASTNode.RestrictiveFlagMASK) == Binding.TYPE) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(QualifiedTypeReference reference, BlockScope scope) {
			if (reference.resolvedType != null) {
				ImportRegistry.this.recordReferenceType(reference.resolvedType);
			}
			return super.visit(reference, scope);
		}

		public boolean visit(QualifiedTypeReference reference, ClassScope scope) {
			if (reference.resolvedType != null) {
				ImportRegistry.this.recordReferenceType(reference.resolvedType);
			}
			return super.visit(reference, scope);
		}

		public boolean visit(SingleNameReference reference, BlockScope scope) {
			if ((reference.bits & ASTNode.RestrictiveFlagMASK) == Binding.TYPE) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(SingleNameReference reference, ClassScope scope) {
			if ((reference.bits & ASTNode.RestrictiveFlagMASK) == Binding.TYPE) {
				if (reference.resolvedType != null) {
					ImportRegistry.this.recordReferenceType(reference.resolvedType);
				}
			}
			return super.visit(reference, scope);
		}

		public boolean visit(SingleTypeReference reference, BlockScope scope) {
			if (reference.resolvedType != null) {
				ImportRegistry.this.recordReferenceType(reference.resolvedType);
			}
			return super.visit(reference, scope);
		}

		public boolean visit(SingleTypeReference reference, ClassScope scope) {
			if (reference.resolvedType != null) {
				ImportRegistry.this.recordReferenceType(reference.resolvedType);
			}
			return super.visit(reference, scope);
		}
		// PREMATURE I believe we have no test case for this one, but inner types
		//           are definitely needed (test384)
		public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
			return visit(localTypeDeclaration, (CompilationUnitScope) null);
		}
		public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
			return visit(memberTypeDeclaration, (CompilationUnitScope) null);
		}
		public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
			if (typeDeclaration.ignoreFurtherInvestigation ||  typeDeclaration.binding == null) return false;
			if (TypeDeclaration.kind(typeDeclaration.modifiers) == TypeDeclaration.ENUM_DECL) {
				ImportRegistry.this.importJavaLangJavaArray = true;
				ImportRegistry.this.importJavaLangEnum = true;
				ImportRegistry.this.importJavaLangSystem = true;
				ImportRegistry.this.importJavaLangClass = true;
			}
			SourceTypeBinding type = typeDeclaration.binding;
			MethodBinding[] methods = type.methods();
			for (int i = 0, l = methods.length, constructorsCount = 0; i < l; i++) {
				if (methods[i].isConstructor()) {
					if (++constructorsCount > 1) {
						ImportRegistry.this.importJavaLangArguments = true;
						break;
					}
				}
			}
			if (type.isAbstract() && !type.isInterface()) { // doing this here loads a bit more imports than strictly needed, but keeps the 'one import registry per cu' viable
				ReferenceBinding[] superInterfaces = type.superInterfaces();
				for (int j = 0, k = superInterfaces == null ? 0 : superInterfaces.length;
					j < k; j++) {
					blend(superInterfaces[j]);
				}
			}
			return true;
		}
	}
	private LinkedHashMap importedTypes = new LinkedHashMap(10);
	private LinkedHashMap unimportedTypes = new LinkedHashMap(10);
	private HashMap ambiguityStatus = new HashMap(10);
	// PREMATURE ImportRegistry is no more an ImportRegistry, since it
	//			 captures other concerns; did not separate that into 
	// 			 another class because of the visitor and the desire to
	// 			 leverage the extraneous first pass at the max, but 
	//           should redesign this (the visitor makes one single extra
	//		 	 pass, but it can and should talk to as many registries as
	//			 needed)
	public boolean importJavaLangArguments = false;
	public boolean importJavaLangClass = false;
	public boolean importJavaLangEnum = false;
	public boolean importJavaLangJavaArray = false;
	public boolean importJavaLangSystem = false;

	PackageBinding currentPackage;
	public Iterator getUnimportedTypes() {
		return this.unimportedTypes.keySet().iterator();
	}
	public void initialize(CompilationUnitDeclaration unit) {
		if (unit.types != null && unit.types.length > 0) {
			TypeDeclaration typeDeclaration = unit.types[0];
			if (typeDeclaration.ignoreFurtherInvestigation) return;
			this.currentPackage = typeDeclaration.binding.getPackage();
			unit.traverse(new TypeReferenceVisitor(), unit.scope);
			if (!this.neededTypes.isEmpty()) {
				// fixing things up - would need improvement
				Hashtable shortNames = new Hashtable();
				Iterator i = this.importedTypes.keySet().iterator();
				while (i.hasNext()) {
					TypeBinding type = (TypeBinding) i.next();
					shortNames.put(new String(type.shortReadableName()), type);
				}
				i = this.unimportedTypes.keySet().iterator();
				while (i.hasNext()) {
					TypeBinding type = (TypeBinding) i.next();
					shortNames.put(new String(type.shortReadableName()), type);
				}
				Enumeration extraNeeded = this.neededTypes.keys();
				while (extraNeeded.hasMoreElements()) {
					TypeBinding type = (TypeBinding) extraNeeded.nextElement();
					if (this.importedTypes.get(type) == null) {
						this.unimportedTypes.put(type, type);
					}
					String key = new String(type.shortReadableName());
					TypeBinding match = (TypeBinding) shortNames.get(key);
					if (match != null) {
						if (match != type) {
							this.ambiguityStatus.put(type, Boolean.TRUE);
							this.ambiguityStatus.put(match, Boolean.TRUE);
						}
					} else {
						shortNames.put(key, type);
					}
				}
				
			}
		}
	}
	private Hashtable blended = new Hashtable();
	private Hashtable neededTypes = new Hashtable();
	void blend(ReferenceBinding type) {
		if (this.blended.get(type) == null) {
			this.blended.put(type, type);
			ReferenceBinding[] superInterfaces = type.superInterfaces();
			for (int j = 0, k = superInterfaces == null ? 0 : superInterfaces.length;
				j < k; j++) {
				blend(superInterfaces[j]);
			}
			TypeBinding neededType;
			FieldBinding[] fields = type.availableFields();
			for(int i = 0, l = fields == null ? 0 : fields.length; i < l; i++) {
				neededType = fields[i].type;
				if (!neededType.isBaseType()) {
					if (neededType.isArrayType()) {
						this.importJavaLangJavaArray = true;
					} else {
						this.neededTypes.put(neededType, neededType /* non null */);
					}
				}
			}
			MethodBinding[] methods = type.availableMethods();
			for(int i = 0, l = methods == null ? 0 : methods.length; i < l; i++) {
				MethodBinding method = methods[i];
				neededType = method.returnType;
				if (!neededType.isBaseType()) {
					if (neededType.isArrayType()) {
						this.importJavaLangJavaArray = true;
					} else {
						this.neededTypes.put(neededType, neededType);
					}
				}
				TypeBinding[] parameters = method.parameters;
				for (int j = 0, pCount = parameters == null ? 0 : parameters.length; j < pCount; j++) {
					neededType = parameters[j];
					if (!neededType.isBaseType()) {
						if (neededType.isArrayType()) {
							this.importJavaLangJavaArray = true;
						} else {
							this.neededTypes.put(neededType, neededType /* non null */);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Indicates whether a binding needs to be qualified
	 * @param type
	 */
	public boolean isAmbiguous(TypeBinding type) {
		switch (type.id) {
			case TypeIds.T_JavaLangObject:
			case TypeIds.T_JavaLangString:
				return false;
		}
		// PREMATURE check the most frequent dynamics; might be that
		//           the lazy initialization mechanism is seldom used,
		//			 which would encourage a short-circuit call to 
		//		     ambiguity status - note that this might well be
		//			 used to make names known (issue of multiple char[]
		//           for the same value); needs extensive white-box testing
		Object name = this.importedTypes.get(type);
		if (name == null) {
			name = this.unimportedTypes.get(type);
			if (name == null) {
				this.recordReferenceType(type);
				return true; // isAmbiguous(type); - should have been known already, hence force qualification (i.e. should be recorded earlier)
			}
		}
		return this.ambiguityStatus.get(name) == Boolean.TRUE;
	}
	
	public void recordImportedType(TypeBinding type) {
		switch (type.id) {
			case TypeIds.T_JavaLangObject:
			case TypeIds.T_JavaLangString:
				return;
		}
		String name = (String) this.importedTypes.get(type);
		if (name != null) return;
		name = new String(type.shortReadableName());
		this.importedTypes.put(type, name);
		Boolean alreadyAmbiguous = (Boolean) this.ambiguityStatus.get(name); 
		if ( alreadyAmbiguous == Boolean.FALSE) { // not first occurrence of simple name ?
			this.ambiguityStatus.put(name, Boolean.TRUE);
		} else if (alreadyAmbiguous == null) {
			Binding inCurrentPackage = this.currentPackage.getTypeOrPackage(name.toCharArray());
			if (inCurrentPackage == null || inCurrentPackage == type || inCurrentPackage instanceof PackageBinding) {
				this.ambiguityStatus.put(name, Boolean.FALSE);
			} else {
				this.ambiguityStatus.put(name, Boolean.TRUE);
			}
		}
	}
	
	public void recordReferenceType(TypeBinding type) {
		switch (type.id) {
			case TypeIds.T_JavaLangObject:
			case TypeIds.T_JavaLangString:
				return;
		}
		if (type.isBaseType()) {
			return;
		}
		if (type.isArrayType()) {
			this.importJavaLangJavaArray = true;
			return;
		}
		String name = (String) this.importedTypes.get(type);
		if (name != null) return; // already imported
		name = (String) this.unimportedTypes.get(type);
		if (name != null) return;
		name = new String(type.shortReadableName());
		this.unimportedTypes.put(type, name);
		Boolean alreadyAmbiguous = (Boolean) this.ambiguityStatus.get(name); 
		if ( alreadyAmbiguous == Boolean.FALSE) { // not first occurrence of simple name ?
			this.ambiguityStatus.put(name, Boolean.TRUE);
		} else if (alreadyAmbiguous == null) {
			Binding inCurrentPackage = this.currentPackage.getTypeOrPackage(name.toCharArray());
			if (inCurrentPackage == null || inCurrentPackage == type || inCurrentPackage instanceof PackageBinding) {
				this.ambiguityStatus.put(name, Boolean.FALSE);
			} else {
				this.ambiguityStatus.put(name, Boolean.TRUE);
			}
		}
	}
}
