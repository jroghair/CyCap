package com.cycapservers.game;

public class Flag extends Item {
	
	protected int team;
	protected boolean atBase;
	protected GridLockedNode base;
	
	public Flag(GridLockedNode node, double w, double h, double r, double a, String entity_id, int team) {
		super(4, 0, node.getX(), node.getY(), w, h, r, a, "Flag", entity_id);
		this.team = team;
		this.base = node;
		this.atBase = true;
		this.spriteIndex = Utils.getSpriteIndexFromTeam(this.team);
	}
	
	public void update() {
		if(this.grabbed) {
			this.x = this.grabber.x;
			this.y = this.grabber.y;
		}
	}

	@Override
	public boolean use() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void pickUp(GameCharacter grabber){
		if(!this.grabbed) {
			if(grabber.team == this.team) {
				if(this.atBase) {
					return;
				}
				else {
					returnToBase();
					grabber.stats.addFlagReturn();
				}
			}
			else {
				this.atBase = false;
				this.grabber = grabber;
				this.grabbed = true;
				grabber.stats.addFlagGrab();
				this.grabber.item_slot = this;
			}
		}
	}
	
	public void returnToBase() {
		if(this.grabber != null) {
			this.grabber.item_slot = null;
		}
		this.grabber = null;
		this.grabbed = false;
		this.x = this.base.getX();
		this.y = this.base.getY();
		this.atBase = true;
	}

}
