package it.exercises.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import it.exercises.ProductCategoryApplication;
import it.exercises.model.io.Category;
import it.exercises.model.io.TreeNode;
import it.exercises.service.TreeNodeService;

@SpringBootTest(classes = ProductCategoryApplication.class)
public class TestTreeNodeService {
	
	   @SpringBootApplication
	   static class TestConfig {}
	   @Autowired TreeNodeService service;
	
	@Test
	public void testNullTree() throws Exception {
		TreeNode tree=null;
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertTrue(retvalue.isEmpty());
	}
	
	@Test
	public void testNullCategory() throws Exception {
		TreeNode tree=new TreeNode();
	    List<Category> retvalue=service.findAllDescendantsBy(null, tree);
	    assertTrue(retvalue.isEmpty());
	}
	
	@Test
	public void testEmptyTree() throws Exception {
		TreeNode tree=new TreeNode();
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertTrue(retvalue.isEmpty());
	}
	
	@Test
	public void testOneNodeMatch() throws Exception {
		TreeNode tree = fillNodeTree(Category.CAT1, null);
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertEquals(1, retvalue.size());
	}

	
	
	
	@Test
	public void testOneNodeNoMatch() throws Exception {
		TreeNode tree = fillNodeTree(Category.CAT2, null);
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertTrue(retvalue.isEmpty());
	}
	
	private TreeNode threeLevelTree() {
		//cat1 --> cat1, cat2 -->cat2
		List<TreeNode> secondLevel=new ArrayList<TreeNode>();
		secondLevel.add(fillNodeTree(Category.CAT1, new ArrayList<TreeNode>()));
		List<TreeNode> thirdLevel=new ArrayList<TreeNode>();
		thirdLevel.add(fillNodeTree(Category.CAT1, new ArrayList<TreeNode>()));
		thirdLevel.add(fillNodeTree(Category.CAT2, new ArrayList<TreeNode>()));
		thirdLevel.add(fillNodeTree(Category.CAT3, new ArrayList<TreeNode>()));
		secondLevel.add(fillNodeTree(Category.CAT2, thirdLevel));
		
		
		TreeNode tree = fillNodeTree(Category.CAT1, secondLevel);
		return tree;
	}
	
	@Test
	public void test3NodeMatchFirst() throws Exception {
		TreeNode tree = threeLevelTree();
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertEquals(6, retvalue.size());
	    assertEquals(Category.CAT1,retvalue.get(0) );
	    assertEquals(Category.CAT1, retvalue.get(1));
	    assertEquals(Category.CAT2, retvalue.get(2));
	    assertEquals(Category.CAT1, retvalue.get(3));
	    assertEquals(Category.CAT2, retvalue.get(4));
	    assertEquals(Category.CAT3, retvalue.get(5) );
		    
	}

	

	@Test
	public void test3NodeMatchSecond() throws Exception {
		TreeNode tree = threeLevelTree();
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT2, tree);
	    assertEquals(4, retvalue.size());
	    assertEquals(Category.CAT2, retvalue.get(0));
	    assertEquals(Category.CAT1, retvalue.get(1));
	    assertEquals(Category.CAT2, retvalue.get(2));
	    assertEquals(Category.CAT3, retvalue.get(3) );
	}

	@Test
	public void test3NodeMatchThird() throws Exception {
		TreeNode tree = threeLevelTree();
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT3, tree);
	    assertEquals(1, retvalue.size());
	    assertEquals(Category.CAT3, retvalue.get(0) );
	}
	@Test
	public void test4NodeMatchDepthLimit() throws Exception {
		TreeNode tree3 = threeLevelTree();
		List<TreeNode> additionalLevel=new ArrayList<TreeNode>();
		additionalLevel.add(tree3);
		TreeNode tree= new TreeNode();
		tree.setChildren(additionalLevel);
		tree.setCategory(Category.CAT2);
	    List<Category> retvalue=service.findAllDescendantsBy(Category.CAT1, tree);
	    assertEquals(3, retvalue.size());
	    assertEquals(Category.CAT1, retvalue.get(0) );
	    assertEquals(Category.CAT1, retvalue.get(1));
	    assertEquals(Category.CAT2, retvalue.get(2));
	}
	private TreeNode fillNodeTree(Category cat, List<TreeNode> children) {
		TreeNode tree=new TreeNode();
		tree.setCategory(cat);
		tree.setChildren(children);
		return tree;
	}
	
}
