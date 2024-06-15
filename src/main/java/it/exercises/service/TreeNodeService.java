package it.exercises.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.exercises.model.io.Category;
import it.exercises.model.io.TreeNode;

@Service
public class TreeNodeService {
	/**
	*
	@param
	category the category of which find all descendants
	*
	@param
	root the root node of the tree
	*
	*
	@return
	the list of all descendants categories, including the category passed as input
	*/
	@Value("${max.tree.node.depth}")
	private int maxTreeNodeDepth;
	
	public List<Category> findAllDescendantsBy (Category category, TreeNode root){
		
		return findAllDescendantsBy(category, root, 0);
	}	
	private List<Category> findAllDescendantsBy (Category category, TreeNode root, int depth){
	
		List<Category> descendants=new ArrayList<Category>();
		if (root !=null && root.getCategory()!=null) {
			if ( root.getCategory().compareTo(category)==0) {
				descendants=findCategories(root, depth+1);
				}
				else if (depth <maxTreeNodeDepth) {
						if (root.getChildren()!=null && !root.getChildren().isEmpty()) {
							for (TreeNode node: root.getChildren()) {
								descendants.addAll(findAllDescendantsBy(category, node, depth+1));
							}
						}
						
				}else {
					//gestione errore
				}
		}
		
		
		return descendants; 
	}	
	
	
	private List<Category> findCategories(TreeNode root, int depth){
		List<Category> cat=new ArrayList<Category>();
		cat.add(root.getCategory());
		if (root.getChildren()!=null ) {
			if (depth <maxTreeNodeDepth) {
				root.getChildren().forEach(child -> cat.addAll(findCategories(child, depth+1)));
			}else {
				//gestione errore
			}
		}
		return cat;
		
	}
}