/*******************************************************************************
 * Copyright (c) 2006, 2010 Soyatec (http://www.soyatec.com) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Soyatec - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.xwt.tools.ui.designer.dialogs;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.xwt.utils.PathHelper;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author jliu (jin.liu@soyatec.com)
 */
public class ImageDialog extends Dialog {

	private IProject project;
	private IFile file;
	private String imagePath;
	private Label imageLabel;
	private Label previewLable;

	private Image image;

	public ImageDialog(Shell parentShell, IFile file) {
		super(parentShell);
		this.file = file;
		if (file != null) {
			this.project = file.getProject();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Image Dialog");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		if (point.y < 343) {
			point.y = 343;
		}
		if (point.x < 440) {
			point.x = 440;
		}
		return point;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		GridLayout layout = (GridLayout) dialogArea.getLayout();
		layout.numColumns = 2;

		Group leftComp = new Group(dialogArea, SWT.NONE);
		leftComp.setText("types");
		layout = new GridLayout(2, false);
		leftComp.setLayout(layout);
		leftComp.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Button projRadio = new Button(leftComp, SWT.RADIO);
		projRadio.setText("Project");
		projRadio.setSelection(true);
		projRadio.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		new Label(leftComp, SWT.NONE)/* .setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.GRAB_VERTICAL)) */;

		final Button fileRadio = new Button(leftComp, SWT.RADIO);
		fileRadio.setText("File System");
		fileRadio.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		final Button browserButton = new Button(leftComp, SWT.PUSH);
		browserButton.setText("Browser");
		// browserButton.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.GRAB_VERTICAL));
		browserButton.setEnabled(false);

		Group previewer = new Group(dialogArea, SWT.NONE);
		previewer.setText("Preview");
		previewer.setLayout(new FillLayout());
		previewLable = new Label(previewer, SWT.CENTER | SWT.SHADOW_IN);
		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.heightHint = 50;
		// gd.widthHint = 50;
		// gd.horizontalSpan = 2;
		previewer.setLayoutData(gd);

		final TreeViewer viewer = new TreeViewer(dialogArea, SWT.BORDER);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalSpan = 2;
		viewer.getControl().setLayoutData(layoutData);
		viewer.setContentProvider(new ImageContentProvider());
		JavaElementLabelProvider labelProvider = new JavaElementLabelProvider() {
			public String getText(Object element) {
				// Need a slight override, for source roots, want to show the entire path so that the project shows up.
				// It doesn't look as good because you can't tell what project the root is in when there are required projects.
				String result = super.getText(element);
				if (element instanceof IPackageFragmentRoot)
					result = MessageFormat.format("{0} - {1}", new Object[] { result, ((IPackageFragmentRoot) element).getJavaProject().getElementName() });
				return result;
			}
		};
		viewer.setLabelProvider(labelProvider);
		if (project != null) {
			viewer.setInput(JavaCore.create(project));
		}
		viewer.expandToLevel(2);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Object firstElement = selection.getFirstElement();
				if (firstElement instanceof IFile) {
					IPath fullPath = ((IFile) firstElement).getLocation();
					String path = fullPath.toString();
					if (file != null) {
						String sourcePath = file.getLocation().toString();
						path = PathHelper.getRelativePath(sourcePath, path);
					}
					setImagePath(path);
				}
			}
		});
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				setImagePath(null);
				if (event.widget == projRadio) {
					viewer.getControl().setEnabled(true);
					browserButton.setEnabled(false);
				} else if (event.widget == fileRadio) {
					viewer.getControl().setEnabled(false);
					browserButton.setEnabled(true);
				} else if (event.widget == browserButton) {
					chooseImage();
				}
			}
		};
		projRadio.addListener(SWT.Selection, listener);
		fileRadio.addListener(SWT.Selection, listener);
		browserButton.addListener(SWT.Selection, listener);

		Composite bottomComp = new Composite(dialogArea, SWT.NONE);
		layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		bottomComp.setLayout(layout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bottomComp.setLayoutData(gd);
		new Label(bottomComp, SWT.NONE).setText("Image:");
		imageLabel = new Label(bottomComp, SWT.NONE);
		imageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return dialogArea;
	}

	protected void chooseImage() {
		FileDialog dialog = new FileDialog(getShell());
		dialog.setFilterExtensions(new String[] { "*.png", "*.gif", "*.*" });
		setImagePath(dialog.open());
	}

	private void setImagePath(String imagePath) {
		this.imagePath = imagePath;
		if (imagePath != null) {
			imageLabel.setText(imagePath);
		} else {
			imageLabel.setText("");
		}
		previewImage(imagePath);
	}

	private void previewImage(String imagePath) {
		if (image != null) {
			image.dispose();
		}
		if (imagePath == null) {
			previewLable.setImage(null);
		} else {
			File imageFile = new File(imagePath);
			if (imageFile.exists()) {
				image = new Image(null, imageFile.getAbsolutePath());
			} else if (file != null) {
				String absolutePath = PathHelper.getAbsolutePath(file.getLocation().toString(), imagePath);
				imageFile = new File(absolutePath);
				if (imageFile.exists()) {
					image = new Image(null, imageFile.getAbsolutePath());
				}
			}
		}
		if (image != null && !image.isDisposed()) {
			previewLable.setImage(image);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	public boolean close() {
		boolean close = super.close();
		if (image != null) {
			image.dispose();
		}
		return close;
	}

	public String getImagePath() {
		return imagePath;
	}

	/*
	 * Content provider for java for our requirements. We extend the standard because it does some important things. We will just fine tune it here.
	 */
	private static class ImageContentProvider extends StandardJavaElementContentProvider {

		private boolean validRoot(IPackageFragmentRoot root) {
			return !root.isExternal() && !root.isArchive();
		}

		/*
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements(Object inputElement) {
			// This would be a project, and since it is the root, we want all packageFragmentRoots, including from required projects.
			try {
				if (inputElement instanceof IJavaProject) {
					IPackageFragmentRoot[] roots = ((IJavaProject) inputElement).getAllPackageFragmentRoots();
					ArrayList<IPackageFragmentRoot> newRoots = new ArrayList<IPackageFragmentRoot>(roots.length);
					for (int i = 0; i < roots.length; i++) {
						if (validRoot(roots[i]))
							newRoots.add(roots[i]);
					}

					return newRoots.toArray();
				}
			} catch (JavaModelException e) {
			}
			return StandardJavaElementContentProvider.NO_CHILDREN;
		}

		/*
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(Object)
		 */
		public Object[] getChildren(Object parentElement) {
			try {
				// I don't need all types, I'm looking specifically only for package fragment roots. They are the only
				// element in this environment that can have children.
				if (parentElement instanceof IPackageFragmentRoot) {
					return ((IPackageFragmentRoot) parentElement).getChildren(); // Get just the package fragments.
				} else if (parentElement instanceof IJavaElement) {
					Object[] children = super.getChildren(parentElement);
					List<Object> result = new ArrayList<Object>();
					for (Object child : children) {
						if (child instanceof IFile) {
							String ext = ((IFile) child).getFileExtension();
							if (ext != null && ("png".equals(ext) || "gif".equals(ext))) {
								result.add(child);
							}
						}
					}
					return result.toArray(new Object[0]);
				}
			} catch (JavaModelException e) {
			}

			return StandardJavaElementContentProvider.NO_CHILDREN;
		}

		/*
		 * @see org.eclipse.jdt.ui.StandardJavaElementContentProvider#hasChildren(Object)
		 */
		public boolean hasChildren(Object element) {
			return super.hasChildren(element);
		}

		protected Object skipProjectPackageFragmentRoot(IPackageFragmentRoot root) {
			// We don't want to skip roots that are the project. We don't show the project in
			// our tree. Because of this we can never programatically select fragments.
			// So instead we return the root as is.
			return root;
		}

	}

}
