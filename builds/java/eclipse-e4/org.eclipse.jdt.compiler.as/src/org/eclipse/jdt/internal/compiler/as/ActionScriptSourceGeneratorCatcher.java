/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.as;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.AnnotationMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayInitializer;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.AssertStatement;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.BinaryExpression;
import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.BreakStatement;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.CharLiteral;
import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.eclipse.jdt.internal.compiler.ast.Clinit;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ContinueStatement;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.EmptyStatement;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.ExtendedStringLiteral;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.FloatLiteral;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;
import org.eclipse.jdt.internal.compiler.ast.ImportReference;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.Javadoc;
import org.eclipse.jdt.internal.compiler.ast.JavadocAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.JavadocArgumentExpression;
import org.eclipse.jdt.internal.compiler.ast.JavadocArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocArraySingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocFieldReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocImplicitTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocMessageSend;
import org.eclipse.jdt.internal.compiler.ast.JavadocQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.JavadocSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.JavadocSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.LabeledStatement;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.MemberValuePair;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NormalAnnotation;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OR_OR_Expression;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.PostfixExpression;
import org.eclipse.jdt.internal.compiler.ast.PrefixExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedSuperReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedThisReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.StringLiteralConcatenation;
import org.eclipse.jdt.internal.compiler.ast.SuperReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.SynchronizedStatement;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeParameter;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.Scope;

// A catcher class to help determine which visitors we still need to
// implement or bypass.
public class ActionScriptSourceGeneratorCatcher extends ASTVisitor {
	private static final boolean CATCH = false;

	/**
	 * @param scope
	 * @param errorMessage
	 * @param location
	 */
	protected void reportError(Scope scope, String errorMessage, ASTNode location) {/**/}
	
	public boolean visit(AllocationExpression allocationExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, allocationExpression.getClass().getName() + " is not supported", allocationExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(AND_AND_Expression and_and_Expression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, and_and_Expression.getClass().getName() + " is not supported", and_and_Expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(AnnotationMethodDeclaration annotationTypeDeclaration, ClassScope classScope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(classScope, annotationTypeDeclaration.getClass().getName() + " is not supported", annotationTypeDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Argument argument, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, argument.getClass().getName() + " is not supported", argument); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Argument argument, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, argument.getClass().getName() + " is not supported", argument); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayAllocationExpression arrayAllocationExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayAllocationExpression.getClass().getName() + " is not supported", arrayAllocationExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayInitializer arrayInitializer, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayInitializer.getClass().getName() + " is not supported", arrayInitializer); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayQualifiedTypeReference.getClass().getName() + " is not supported", arrayQualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayQualifiedTypeReference arrayQualifiedTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayQualifiedTypeReference.getClass().getName() + " is not supported", arrayQualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayReference arrayReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayReference.getClass().getName() + " is not supported", arrayReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayTypeReference arrayTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayTypeReference.getClass().getName() + " is not supported", arrayTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ArrayTypeReference arrayTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, arrayTypeReference.getClass().getName() + " is not supported", arrayTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(AssertStatement assertStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, assertStatement.getClass().getName() + " is not supported", assertStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Assignment assignment, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, assignment.getClass().getName() + " is not supported", assignment); //$NON-NLS-1$
		return false;
	}

	public boolean visit(BinaryExpression binaryExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, binaryExpression.getClass().getName() + " is not supported", binaryExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Block block, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, block.getClass().getName() + " is not supported", block); //$NON-NLS-1$
		return false;
	}

	public boolean visit(BreakStatement breakStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, breakStatement.getClass().getName() + " is not supported", breakStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(CaseStatement caseStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, caseStatement.getClass().getName() + " is not supported", caseStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(CastExpression castExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, castExpression.getClass().getName() + " is not supported", castExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(CharLiteral charLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, charLiteral.getClass().getName() + " is not supported", charLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ClassLiteralAccess classLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, classLiteral.getClass().getName() + " is not supported", classLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Clinit clinit, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, clinit.getClass().getName() + " is not supported", clinit); //$NON-NLS-1$
		return false;
	}

	public boolean visit(CompilationUnitDeclaration compilationUnitDeclaration, CompilationUnitScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, compilationUnitDeclaration.getClass().getName() + " is not supported", compilationUnitDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(CompoundAssignment compoundAssignment, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, compoundAssignment.getClass().getName() + " is not supported", compoundAssignment); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ConditionalExpression conditionalExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, conditionalExpression.getClass().getName() + " is not supported", conditionalExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, constructorDeclaration.getClass().getName() + " is not supported", constructorDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ContinueStatement continueStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, continueStatement.getClass().getName() + " is not supported", continueStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(DoStatement doStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, doStatement.getClass().getName() + " is not supported", doStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(DoubleLiteral doubleLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, doubleLiteral.getClass().getName() + " is not supported", doubleLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(EmptyStatement emptyStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, emptyStatement.getClass().getName() + " is not supported", emptyStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(EqualExpression equalExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, equalExpression.getClass().getName() + " is not supported", equalExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ExplicitConstructorCall explicitConstructor, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, explicitConstructor.getClass().getName() + " is not supported", explicitConstructor); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ExtendedStringLiteral extendedStringLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, extendedStringLiteral.getClass().getName() + " is not supported", extendedStringLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FalseLiteral falseLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, falseLiteral.getClass().getName() + " is not supported", falseLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, fieldDeclaration.getClass().getName() + " is not supported", fieldDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FieldReference fieldReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, fieldReference.getClass().getName() + " is not supported", fieldReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FieldReference fieldReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, fieldReference.getClass().getName() + " is not supported", fieldReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(FloatLiteral floatLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, floatLiteral.getClass().getName() + " is not supported", floatLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ForeachStatement forStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, forStatement.getClass().getName() + " is not supported", forStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ForStatement forStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, forStatement.getClass().getName() + " is not supported", forStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(IfStatement ifStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, ifStatement.getClass().getName() + " is not supported", ifStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ImportReference importRef, CompilationUnitScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, importRef.getClass().getName() + " is not supported", importRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Initializer initializer, MethodScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, initializer.getClass().getName() + " is not supported", initializer); //$NON-NLS-1$
		return false;
	}

	public boolean visit(InstanceOfExpression instanceOfExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, instanceOfExpression.getClass().getName() + " is not supported", instanceOfExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(IntLiteral intLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, intLiteral.getClass().getName() + " is not supported", intLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Javadoc javadoc, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, javadoc.getClass().getName() + " is not supported", javadoc); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Javadoc javadoc, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, javadoc.getClass().getName() + " is not supported", javadoc); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocAllocationExpression expression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, expression.getClass().getName() + " is not supported", expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocAllocationExpression expression, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, expression.getClass().getName() + " is not supported", expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArgumentExpression expression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, expression.getClass().getName() + " is not supported", expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArgumentExpression expression, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, expression.getClass().getName() + " is not supported", expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArrayQualifiedTypeReference typeRef, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArrayQualifiedTypeReference typeRef, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArraySingleTypeReference typeRef, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocArraySingleTypeReference typeRef, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocFieldReference fieldRef, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, fieldRef.getClass().getName() + " is not supported", fieldRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocFieldReference fieldRef, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, fieldRef.getClass().getName() + " is not supported", fieldRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocImplicitTypeReference implicitTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, implicitTypeReference.getClass().getName() + " is not supported", implicitTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocImplicitTypeReference implicitTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, implicitTypeReference.getClass().getName() + " is not supported", implicitTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocMessageSend messageSend, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, messageSend.getClass().getName() + " is not supported", messageSend); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocMessageSend messageSend, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, messageSend.getClass().getName() + " is not supported", messageSend); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocQualifiedTypeReference typeRef, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocQualifiedTypeReference typeRef, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocReturnStatement statement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, statement.getClass().getName() + " is not supported", statement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocReturnStatement statement, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, statement.getClass().getName() + " is not supported", statement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocSingleNameReference argument, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, argument.getClass().getName() + " is not supported", argument); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocSingleNameReference argument, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, argument.getClass().getName() + " is not supported", argument); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocSingleTypeReference typeRef, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(JavadocSingleTypeReference typeRef, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeRef.getClass().getName() + " is not supported", typeRef); //$NON-NLS-1$
		return false;
	}

	public boolean visit(LabeledStatement labeledStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, labeledStatement.getClass().getName() + " is not supported", labeledStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(LocalDeclaration localDeclaration, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, localDeclaration.getClass().getName() + " is not supported", localDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(LongLiteral longLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, longLiteral.getClass().getName() + " is not supported", longLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(MarkerAnnotation annotation, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, annotation.getClass().getName() + " is not supported", annotation); //$NON-NLS-1$
		return false;
	}

	public boolean visit(MemberValuePair pair, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, pair.getClass().getName() + " is not supported", pair); //$NON-NLS-1$
		return false;
	}

	public boolean visit(MessageSend messageSend, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, messageSend.getClass().getName() + " is not supported", messageSend); //$NON-NLS-1$
		return false;
	}

	public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, methodDeclaration.getClass().getName() + " is not supported", methodDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(NormalAnnotation annotation, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, annotation.getClass().getName() + " is not supported", annotation); //$NON-NLS-1$
		return false;
	}

	public boolean visit(NullLiteral nullLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, nullLiteral.getClass().getName() + " is not supported", nullLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(OR_OR_Expression or_or_Expression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, or_or_Expression.getClass().getName() + " is not supported", or_or_Expression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, parameterizedQualifiedTypeReference.getClass().getName() + " is not supported", parameterizedQualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ParameterizedQualifiedTypeReference parameterizedQualifiedTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, parameterizedQualifiedTypeReference.getClass().getName() + " is not supported", parameterizedQualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, parameterizedSingleTypeReference.getClass().getName() + " is not supported", parameterizedSingleTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ParameterizedSingleTypeReference parameterizedSingleTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, parameterizedSingleTypeReference.getClass().getName() + " is not supported", parameterizedSingleTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(PostfixExpression postfixExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, postfixExpression.getClass().getName() + " is not supported", postfixExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(PrefixExpression prefixExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, prefixExpression.getClass().getName() + " is not supported", prefixExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedAllocationExpression qualifiedAllocationExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedAllocationExpression.getClass().getName() + " is not supported", qualifiedAllocationExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedNameReference qualifiedNameReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedNameReference.getClass().getName() + " is not supported", qualifiedNameReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedNameReference qualifiedNameReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedNameReference.getClass().getName() + " is not supported", qualifiedNameReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedSuperReference qualifiedSuperReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedSuperReference.getClass().getName() + " is not supported", qualifiedSuperReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedSuperReference qualifiedSuperReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedSuperReference.getClass().getName() + " is not supported", qualifiedSuperReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedThisReference qualifiedThisReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedThisReference.getClass().getName() + " is not supported", qualifiedThisReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedThisReference qualifiedThisReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedThisReference.getClass().getName() + " is not supported", qualifiedThisReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedTypeReference qualifiedTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedTypeReference.getClass().getName() + " is not supported", qualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(QualifiedTypeReference qualifiedTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, qualifiedTypeReference.getClass().getName() + " is not supported", qualifiedTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ReturnStatement returnStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, returnStatement.getClass().getName() + " is not supported", returnStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SingleMemberAnnotation annotation, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, annotation.getClass().getName() + " is not supported", annotation); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SingleNameReference singleNameReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, singleNameReference.getClass().getName() + " is not supported", singleNameReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SingleNameReference singleNameReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, singleNameReference.getClass().getName() + " is not supported", singleNameReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SingleTypeReference singleTypeReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, singleTypeReference.getClass().getName() + " is not supported", singleTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SingleTypeReference singleTypeReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, singleTypeReference.getClass().getName() + " is not supported", singleTypeReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(StringLiteral stringLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, stringLiteral.getClass().getName() + " is not supported", stringLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(StringLiteralConcatenation literal, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, literal.getClass().getName() + " is not supported", literal); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SuperReference superReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, superReference.getClass().getName() + " is not supported", superReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SwitchStatement switchStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, switchStatement.getClass().getName() + " is not supported", switchStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(SynchronizedStatement synchronizedStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, synchronizedStatement.getClass().getName() + " is not supported", synchronizedStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ThisReference thisReference, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, thisReference.getClass().getName() + " is not supported", thisReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ThisReference thisReference, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, thisReference.getClass().getName() + " is not supported", thisReference); //$NON-NLS-1$
		return false;
	}

	public boolean visit(ThrowStatement throwStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, throwStatement.getClass().getName() + " is not supported", throwStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TrueLiteral trueLiteral, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, trueLiteral.getClass().getName() + " is not supported", trueLiteral); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TryStatement tryStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, tryStatement.getClass().getName() + " is not supported", tryStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, memberTypeDeclaration.getClass().getName() + " is not supported", memberTypeDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeDeclaration.getClass().getName() + " is not supported", typeDeclaration); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TypeParameter typeParameter, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeParameter.getClass().getName() + " is not supported", typeParameter); //$NON-NLS-1$
		return false;
	}

	public boolean visit(TypeParameter typeParameter, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, typeParameter.getClass().getName() + " is not supported", typeParameter); //$NON-NLS-1$
		return false;
	}

	public boolean visit(UnaryExpression unaryExpression, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, unaryExpression.getClass().getName() + " is not supported", unaryExpression); //$NON-NLS-1$
		return false;
	}

	public boolean visit(WhileStatement whileStatement, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, whileStatement.getClass().getName() + " is not supported", whileStatement); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Wildcard wildcard, BlockScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, wildcard.getClass().getName() + " is not supported", wildcard); //$NON-NLS-1$
		return false;
	}

	public boolean visit(Wildcard wildcard, ClassScope scope) {
		if (CATCH) {
			throw new RuntimeException();
		}
		reportError(scope, wildcard.getClass().getName() + " is not supported", wildcard); //$NON-NLS-1$
		return false;
	}
}
