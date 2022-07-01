package com.example.jsondownloadapp;

import java.io.Serializable;

public class Hero implements Serializable {
	private String name;
	private int hp;
	private int mp;
	
	public Hero() {
		super();
	}

	public Hero(String name, int hp, int mp) {
		super();
		this.name = name;
		this.hp = hp;
		this.mp = mp;
	}

	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}

	public int getMp() {
		return mp;
	}
}
