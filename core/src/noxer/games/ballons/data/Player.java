package noxer.games.ballons.data;

import noxer.games.ballons.entities.Ball;
import com.badlogic.gdx.utils.Array;

public class Player {
	public Array<Ball> balls;//or arraylist
	public Array<Ball> ballsAlive; 
	public int padStyle;
	
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
}
