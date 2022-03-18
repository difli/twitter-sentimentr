package io.flickd.twitter.ui.model;


public class ApplicationInfo {
    private String[] profiles;
    private String[] services;
    private String twitter_query;
    private String ip;
    private int port;
 
    public ApplicationInfo(String[] profiles, String[] services, String twitter_query, String ip, int port) {
        this.profiles = profiles;
        this.services = services;
        this.twitter_query = twitter_query;
        this.ip = ip;
        this.port = port;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }

    public String[] getServices() {
        return services;
    }

    public void setServices(String[] services) {
        this.services = services;
    }

	public String getIp() {
		return ip;
	}
 
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTwitter_query() {
		return twitter_query;
	}

	public void setTwitter_query(String twitter_query) {
		this.twitter_query = twitter_query;
	}
    
}