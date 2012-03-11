/**
 * 
 */
package org.eclipse.e4.tm.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

class EClassNameBinderFactory extends BinderFactory {
	
	public EClassNameBinderFactory(AbstractBuilder builder) {
		super(builder);
	}

	protected IBinder createBinder(EClass eClass) {
		List<EClass> superTypes = new ArrayList<EClass>(eClass.getEAllSuperTypes());
		superTypes.add(0, eClass);
		for (Iterator<EClass> it = superTypes.iterator(); it.hasNext();) {
			EClass superClass = it.next();
			IBinder binder = super.createBinder(superClass);
			if (binder != null) {
				return binder;
			}
		}
		return null;
	}
}
