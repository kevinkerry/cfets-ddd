package com.cfets.ddd.d.platform.repository.event;

public class MergeRequestCreateEvent {
private String id;


public MergeRequestCreateEvent(String id) {
	System.out.println("begin init MergeRequestCreateEvent 0000000000"+id);
	this.id = id;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

}
