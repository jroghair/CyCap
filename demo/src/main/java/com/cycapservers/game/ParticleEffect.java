package com.cycapservers.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ParticleEffect {
	
	protected List<Particle> particles;
	
	public ParticleEffect() {
		this.particles = new ArrayList<Particle>();
	}
	
	public void update() {
		//TODO
		/*
		ListIterator<Particle> iter = this.particles.listIterator();
		while(iter.hasNext()){
			Particle temp = iter.next();
		    if(temp.update()) {
		    	iter.remove(); //remove the particle from the effect when it's finished
		    }
		}
		*/
	}

}
