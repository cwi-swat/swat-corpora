/**
 * 
 */
package org.eclipse.e4.emf.javascript.ui;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

public class StringEditorInput implements IStorageEditorInput {

	private String id;

	protected final String scriptSource;
	protected final IPath storagePath;

	public StringEditorInput(String id, IPath storagePath, String source) {
		this.id = id;
		this.storagePath = storagePath;
		this.scriptSource = source;
	}

	public boolean equals(Object other) {
		return other instanceof StringEditorInput && id.equals(((StringEditorInput)other).id);
	}
	
	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getToolTipText() {
		return id;
	}

	public IPersistableElement getPersistable() {
		return null; // new Element();
	}
	
//	public static class Element implements IPersistableElement, IAdaptable {
//		public Object getAdapter(Class adapter) {
//			System.out.println(adapter);
//			return null;
//		}
//		public void saveState(IMemento memento) {
//		}
//		public String getFactoryId() {
//			return StringEditorInput.class.getName();
//		}
//	}
//	public static class Factory implements IElementFactory {
//		public IAdaptable createElement(IMemento memento) {
//			return new Element();
//		}
//	}

	public String getName() {
		int pos = id.lastIndexOf('/');
		return (pos > 0 ? id.substring(pos + 1) : id);
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public boolean exists() {
		return true;
	}

	public IStorage getStorage() {
		return new IStorage() {
			public Object getAdapter(Class adapter) {
				return null;
			}
			public boolean isReadOnly() {
				return false;
			}
			public String getName() {
				return StringEditorInput.this.getName();
			}
			public IPath getFullPath() {
				return storagePath;
			}
			
			public InputStream getContents() throws CoreException {
				return new StringBufferInputStream(scriptSource);
			}
		};
	}
}
