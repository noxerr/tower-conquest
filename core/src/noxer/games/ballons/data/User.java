package noxer.games.ballons.data;

import noxer.games.ballons.entities.Ball;

import com.badlogic.gdx.utils.Array;

public class User extends Player{

	public User(Ball balls[]){
		super(balls);
		padStyle = 0; //TODO ADD STYLE ON CONSTRUCTOR AND CHECK STYLE WHEN STARTING GAME
	}
	
	public User(Array<Ball> arr) {
		super(arr);
		// TODO Auto-generated constructor stub
	}
}
