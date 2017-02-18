package com.hjt.phoneshow.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelList extends BaseEntity {
	private ArrayList<Data> data = new ArrayList<Data>();

	public ArrayList<Data> getData() {
		return data;
	}

	public void setData(ArrayList<Data> data) {
		this.data.clear();
		this.data.addAll(data);
	}

	public class Data {
		public ArrayList<List> list = new ArrayList<>();

		public void setList(ArrayList<List> list) {
			this.list.clear();
			this.list.addAll(list);
		}

		public ArrayList<List> getList() {
			return list;
		}
	}

	public class List implements Serializable {
		private String id;
		private String name;
		private String icon;
		private String time;
		private String tips;
		public String getTips() {
			return tips;
		}

		public void setTips(String tips) {
			this.tips = tips;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
	}
}
