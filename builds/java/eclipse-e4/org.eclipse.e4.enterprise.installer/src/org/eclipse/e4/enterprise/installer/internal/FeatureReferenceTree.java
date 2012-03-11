/******************************************************************************
 * Copyright (c) David Orme and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Orme - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.enterprise.installer.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.enterprise.installer.Activator;
import org.eclipse.e4.enterprise.installer.FeatureVersionedIdentifier;
import org.eclipse.e4.enterprise.installer.InstallError;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.core.IFeatureReference;
import org.eclipse.update.core.IIncludedFeatureReference;

/**
 * Represents the tree of IFeatureReferences obtained by walking all 
 * features included in features in features (, ...) on update site(s).
 */
public class FeatureReferenceTree {
	
	private ILog logger = null;
	
	private List<IFeatureReference> referenceList = new ArrayList<IFeatureReference>();
	private TreeNode root = new TreeNode();

	public FeatureReferenceTree() {
		logger = Activator.getDefault().getLog();
	}
	
	public List<IFeatureReference> getAllReferences() {
		return Collections.unmodifiableList(referenceList);
	}

	public List<IFeatureReference> getFeatureReferenceWithDescendants(FeatureVersionedIdentifier key) {
		TreeNode node = findNode(key);
		if (null == node) {
			return Collections.emptyList();
		}

		// Get all its descendants
		List<IFeatureReference> result = new ArrayList<IFeatureReference>();
		ArrayList<TreeNode> nodesToInspect = new ArrayList<TreeNode>();
		nodesToInspect.add(node);
		collectAllDescendants(nodesToInspect, result);

		return result;
	}
	
	public IFeatureReference getFeatureReference(FeatureVersionedIdentifier key) {
		TreeNode node = findNode(key);
		
		if(node == null ) {
			return null;
		}
		
		return node.payload;
	}

	private TreeNode findNode(FeatureVersionedIdentifier key) {
		ArrayList<TreeNode> searchSpace = new ArrayList<TreeNode>();
		searchSpace.addAll(root.children);
		TreeNode node = find(key, searchSpace);
		return node;
	}
	
	
	private void collectAllDescendants(List<TreeNode> nodesToInspect, List<IFeatureReference> result) {
		if (nodesToInspect.isEmpty()) {
			return;
		}
		TreeNode head = nodesToInspect.get(0);
		result.add(head.payload);
		nodesToInspect.remove(0);
		nodesToInspect.addAll(head.children);
		collectAllDescendants(nodesToInspect, result);
		return;
	}

	private TreeNode find(FeatureVersionedIdentifier key, List<TreeNode> searchSpace) {
		if (searchSpace.isEmpty()) {
			return null;
		}

		TreeNode head = searchSpace.get(0);
		if (head.id.equals(key)) {
			return head;
		} else {
			searchSpace.remove(0);
			searchSpace.addAll(head.children);
			return find(key, searchSpace);
		}
	}

	public void add(IFeatureReference reference) throws InstallError {
		add(reference, root);
	}

	private void add(IFeatureReference reference, TreeNode tree) throws InstallError {
		try {
			IFeature feature = reference.getFeature(null);
			IIncludedFeatureReference[] includedFeatureReferences;
			try {
				includedFeatureReferences = feature.getIncludedFeatureReferences();
			} catch (Exception e) {
				logger.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "FeatureReferenceTree.add(): Could not resolve included features for feature:"+feature.getLabel()));
				includedFeatureReferences = new IIncludedFeatureReference[]{};
			}

			TreeNode newNode = new TreeNode();
			newNode.id = new FeatureVersionedIdentifier(reference);
			newNode.payload = reference;
			tree.children.add(newNode);

			referenceList.add(reference);

			if (includedFeatureReferences.length > 0) {
				for (int i = 0; i < includedFeatureReferences.length; i++) {
					add(includedFeatureReferences[i], newNode);
				}
			}

		} catch (CoreException e) {
			logger.log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "FeatureReferenceTree.add(): CoreException translated to InstallError: "+e.getMessage()));
			throw new InstallError(e);
		}
	}

	private class TreeNode {
		public FeatureVersionedIdentifier id;
		public IFeatureReference payload;
		public List<TreeNode> children = new ArrayList<TreeNode>();
	}

}
