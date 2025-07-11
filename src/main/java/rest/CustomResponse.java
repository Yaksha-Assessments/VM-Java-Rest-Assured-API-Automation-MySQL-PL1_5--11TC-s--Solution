package rest;

import java.util.List;
import java.util.Map;

import io.restassured.response.Response;

public class CustomResponse {

    private Response response;
    private int statusCode;
    private String status;
    private List<Object> ids;
    private List<Object> unitIds;
    private List<Object> names;
    private List<Object> levels;
    private List<Object> children;
    private List<Object> descriptions;
    private List<Object> id;
    private Object title2;
    private List<Object> titles;
    private List<Object> title;
    private Object description;
    private Object id2;
    private Object note;
    private Map<String, Object> jobSpecification;
    

    // Constructor
    public CustomResponse(Response response, int statusCode, String status,
                          List<Object> ids, List<Object> unitIds,
                          List<Object> names, List<Object> levels,
                          List<Object> children) {
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.unitIds = unitIds;
        this.names = names;
        this.levels = levels;
        this.children = children;
    }
    

    //"response", "statusCode", "status", "ids", "unitIds", "names", "levels", "children"

    public CustomResponse(Response response, int statusCode, String status, List<Object> ids, List<Object> names) {
    	
    	this.response = response;
    	this.statusCode = statusCode;
    	this.status = status;
    	this.ids = ids;
    	this.names = names;
		// TODO Auto-generated constructor stub
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> ids) {
		this.response = response;
    	this.statusCode = statusCode;
    	this.status = status;
    	this.ids = ids;
    	
		// TODO Auto-generated constructor stub
	}

	public CustomResponse(Response response, int statusCode, String status, List<Object> id, List<Object> titles,
			Object description, Object note, Map<String, Object> jobSpecification) {
		 this.response = response;
	        this.statusCode = statusCode;
	        this.status = status;
	        this.id = id;
	        this.titles = titles;
	        this.description = description;
	        this.note = note;
	        this.jobSpecification = jobSpecification;
		
		// TODO Auto-generated constructor stub
	}
	
	public CustomResponse(Response response, int statuCode, String status, List<Object> ids, List<Object> unitIds, List<Object> names, List<Object> levels) {
		// TODO Auto-generated constructor stub
        this.response = response;
        this.statusCode = statusCode;
        this.status = status;
        this.ids = ids;
        this.unitIds = unitIds;
        this.names = names;
        this.levels = levels;
	}
	
	   
	    
	  
	    private Object name;

	    public CustomResponse(Response response2, int statusCode2, String status2, Object id2, Object name) {
	        this.response = response2;
	        this.statusCode = statusCode2;
	        this.status = status2;
	        this.id2 = id2;
	        this.name = name;
	    }

	    // Optional: Add getters and setters
	   

	   
	   
	    public CustomResponse(Response response2, int statusCode2, String status2) {
	    	
	    	this.response = response2;
	    	this.statusCode= statusCode2;
	    	this.status = status2;
			// TODO Auto-generated constructor stub
		}


		public CustomResponse(Response response2, int statusCode2, String status2, List<Object> ids2,
				List<Object> names2, List<Object> descriptions2) {
			
			this.response= response2;
			this.statusCode = statusCode2;
		    this.status = status2;
		    this.ids = ids2;
		    this.names = names2;
		    this.descriptions = descriptions2;
		
			// TODO Auto-generated constructor stub
		}


		public CustomResponse(Response response2, int statusCode2, String status2, Object title2) {
			this.response= response2;
			this.statusCode = statusCode2;
			this.status= status2;
			this.title2=title2;
			// TODO Auto-generated constructor stub
		}


		public Object getId2() {
	        return id2;
	    }

	    public void setId(Object id2) {
	        this.id2 = id2;
	    }

	    public Object getName() {
	        return name;
	    }

	    public void setName(Object name) {
	        this.name = name;
	    }
	



	public  Object getTitle2() {
	    return title2;
	}

	public void setTitle(List<Object> title) {
	    this.title = title;
	}
	public  Object getTitles() {
	    return titles;
	}

	public void setTitles(List<Object> title) {
	    this.titles = titles;
	}
	// Getters and Setters
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getIds() {
        return ids;
    }

    public void setIdss(List<Object> id) {
        this.id = id;
    }
    public List<Object> getIdss() {
        return id;
    }

    public void setIds(List<Object> ids) {
        this.ids = ids;
    }

    public List<Object> getUnitIds() {
        return unitIds;
    }

    public void setUnitIds(List<Object> unitIds) {
        this.unitIds = unitIds;
    }

    public List<Object> getNames() {
        return names;
    }

    public void setNames(List<Object> names) {
        this.names = names;
    }

    public List<Object> getLevels() {
        return levels;
    }

    public void setLevels(List<Object> levels) {
        this.levels = levels;
    }

    public List<Object> getChildren() {
        return children;
    }

    public void setChildren(List<Object> children) {
        this.children = children;
    }
    public List<Object> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<Object> descriptions) {
        this.descriptions = descriptions;
    }
}

