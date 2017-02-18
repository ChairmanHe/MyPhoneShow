package com.hjt.phoneshow.bean;

import java.util.ArrayList;

public class Detail extends BaseEntity {
	private ArrayList<Info> data = new ArrayList<Info>();

	public Info getData() {
		return (data == null || data.size() == 0) ? new Info() : data.get(0);
	}

	public void setData(ArrayList<Info> data) {
		this.data.clear();
		this.data.addAll(data);
	}

	public void addInfo(Info data) {
		this.data.add(data);
	}

	public class Info {
		private ArrayList<String> diagram_img = new ArrayList<String>();// 简图
		private ArrayList<String> base_img = new ArrayList<String>();// 基本图
		private ArrayList<String> parameter_img = new ArrayList<String>();// 规格图
		private ArrayList<String> summary_img = new ArrayList<String>();// 概述图

		public ArrayList<String> getDiagram_img() {
			return diagram_img;
		}

		public void setDiagram_img(ArrayList<String> diagram_img) {
			this.diagram_img.clear();
			this.diagram_img.addAll(diagram_img);
		}

		public ArrayList<String> getBase_img() {
			return base_img;
		}

		public void setBase_img(ArrayList<String> base_img) {
			this.base_img.clear();
			this.base_img.addAll(base_img);
		}

		public ArrayList<String> getParameter_img() {
			return parameter_img;
		}

		public void setParameter_img(ArrayList<String> parameter_img) {

			this.parameter_img.clear();
			this.parameter_img.addAll(parameter_img);
		}

		public ArrayList<String> getSummary_img() {
			return summary_img;
		}

		public void setSummary_img(ArrayList<String> summary_img) {

			this.summary_img.clear();
			this.summary_img.addAll(summary_img);
		}
	}
}
