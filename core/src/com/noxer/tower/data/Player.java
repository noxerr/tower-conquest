package com.noxer.tower.data;


import com.badlogic.gdx.utils.Array;
import com.noxer.tower.entities.Ball;
import com.noxer.tower.entitiesAI.Tower;

public class Player {
	public Array<Ball> balls;//or arraylist
	public Array<Ball> ballsAlive; 
	public int padStyle;
	public Tower tower;
	
	public Player(Ball balls[]){
		this.balls = new Array<Ball>(balls);
		ballsAlive = new Array<Ball>(this.balls);
	}
	
	public Player(Array<Ball> arr){
		this.balls = new Array<Ball>(arr);
	}
	
	public boolean addBall(Ball ball){
		if (balls.size < 10) {
			balls.add(ball);
			return true;
		}
		else return false;
	}
	
	public void restore(Ball balls[], boolean empty){
		if (!empty) this.balls = new Array<Ball>(balls);
	}
	
	public Ball[] getBalls(){
		return balls.items;
	}
	
	public void addTower(Tower tower){
		this.tower = tower;
	}
}
