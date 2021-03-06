package uk.org.mattford.scoutlink.model;

import com.google.common.collect.ImmutableList;

import org.pircbotx.ChannelListEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class Server {

	private HashMap<String, Conversation> conversations;
	private int status = 0;
    private ArrayList<String> channelList = new ArrayList<>();
	
	public final static int STATUS_DISCONNECTED = 0;
	public final static int STATUS_CONNECTED = 1;
	
	public Server() {
		this.conversations = new HashMap<>();
	}
	
	public Conversation getConversation(String name) {

		if (conversations.containsKey(name)) {
			return conversations.get(name);
		} else {
			return null;
		}
	}

    public void setChannelList(ImmutableList<ChannelListEntry> channels) {
        channelList.clear();
        for (ChannelListEntry entry : channels) {
            if (entry.getName().startsWith("#")) {
                channelList.add(entry.getName());
            }
        }
    }

    public ArrayList<String> getChannelList() {
        return channelList;
    }
	
	public int getStatus() {
		return this.status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public HashMap<String, Conversation> getConversations() {
		return this.conversations;
	}
	
	public void addConversation(Conversation conv) {
		conversations.put(conv.getName(), conv);
	}
	
	public void removeConversation(String name) {
		conversations.remove(name);
	}
	
	public void clearConversations() {
		conversations.clear();
	}
	
	
}
