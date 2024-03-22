package com.ACC.MobilePlanPrice.model;
 
public class PageRanking {

	private String webPageName;
	private int score;
	public String getWebPageName() {
		return webPageName;
	}
	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public PageRanking() {
	}
	public PageRanking(String webPageName, int score) {
		super();
		this.webPageName = webPageName;
		this.score = score;
	}



 
}