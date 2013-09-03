package com.jknyou.redis.webflow;

import java.io.Serializable;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.mindmap.DefaultMindmapNode;
import org.primefaces.model.mindmap.MindmapNode;
import org.springframework.stereotype.Component;

@Component
public class MindmapBean implements Serializable {
	private static final long serialVersionUID = 8959433053720954650L;

	private MindmapNode root;

	private MindmapNode selectedNode;

	public MindmapBean() {
		root = new DefaultMindmapNode("jknyou", "Google WebSite", "FFCC00", false);

		MindmapNode tester1 = new DefaultMindmapNode("tester1", "tester1", "6e9ebf", true);
		MindmapNode tester2 = new DefaultMindmapNode("tester2", "tester2", "6e9ebf", true);
		MindmapNode tester3 = new DefaultMindmapNode("tester3", "tester3", "6e9ebf", true);

		MindmapNode tester4 = new DefaultMindmapNode("tester4", "tester4", "6e9ebf", true);
		MindmapNode tester5 = new DefaultMindmapNode("tester5", "tester5", "6e9ebf", true);
		
		tester1.addNode(tester4);
		tester1.addNode(tester5);

		root.addNode(tester1);
		root.addNode(tester2);
		root.addNode(tester3);
		
	}

	public MindmapNode getRoot() {
		return root;
	}

	public MindmapNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(MindmapNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public void onNodeSelect(SelectEvent event) {
//		MindmapNode node = (MindmapNode) event.getObject();
//
//		// populate if not already loaded
//		if (node.getChildren().isEmpty()) {
//			Object label = node.getLabel();
//
//			if (label.equals("NS(s)")) {
//				for (int i = 0; i < 25; i++) {
//					node.addNode(new DefaultMindmapNode("ns" + i + ".google.com", "Namespace " + i + " of Google", "82c542"));
//				}
//			} else if (label.equals("IPs")) {
//				for (int i = 0; i < 18; i++) {
//					node.addNode(new DefaultMindmapNode("1.1.1." + i, "IP Number: 1.1.1." + i, "fce24f"));
//				}
//
//			} else if (label.equals("Malware")) {
//				for (int i = 0; i < 18; i++) {
//					String random = UUID.randomUUID().toString();
//					node.addNode(new DefaultMindmapNode("Malware-" + random, "Malicious Software: " + random, "3399ff", false));
//				}
//			}
//		}

	}

	public void onNodeDblselect(SelectEvent event) {
		this.selectedNode = (MindmapNode) event.getObject();
	}
}
