package org.eclipse.e4.emf.ecore.javascript;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


public class EPackageRegistryBuilder extends IncrementalProjectBuilder {

	protected IProject[] build(int kind, Map args, final IProgressMonitor monitor) throws CoreException {
		IProject project = this.getProject();
		IResourceDelta delta = getDelta(project);
		if (delta == null) {
			return null;
		}
		delta.accept(new IResourceDeltaVisitor() {

			public boolean visit(IResourceDelta delta) throws CoreException {
				IResource resource = delta.getResource();
				if (resource instanceof IFile && "ecore".equals(resource.getFileExtension())) {
					URI ecoreUri = URI.createPlatformResourceURI(resource.getFullPath().toString(), true);
					Resource ecoreResource = new ResourceSetImpl().getResource(ecoreUri, true);
					registerPackages(ecoreResource.getContents(), monitor);
					return false;
				}
				return true;
			}
		});
		return null;
	}
	
	private void registerPackages(List<EObject> contents, IProgressMonitor monitor) {
		for (EObject eObject: contents) {
			if (eObject instanceof EPackage) {
				registerEPackage((EPackage)eObject);
				registerPackages(eObject.eContents(), monitor);
			}
		}
	}

	private void registerEPackage(EPackage ePack) {
		Registry registry = EPackage.Registry.INSTANCE;
		String nsUri = ePack.getNsURI();
		if (nsUri != null && nsUri.length() != 0 && (! registry.containsKey(nsUri))) {
			System.out.println("Mapping " + nsUri + " to " + ePack);
			registry.put(nsUri, ePack);
		}
	}
}
