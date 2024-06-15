package it.exercises.model.io;

import java.util.List;

/**
* A node of the tree.
*/
public class TreeNode{
	private Category category;
	private List<TreeNode> children;
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	
}
