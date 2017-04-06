package com.cgi.nikoniko.controllers.PathClass;

public class PathFinder {

	public final static int LENGHT_VIEW = 5;

	public final static String DOT = ".";
	public final static String PATH = "/";

	public static final String SHOW_PATH = "show";
	public final static String SHOW_LINK = "link";
	public final static String REDIRECT = "redirect:";

	public final static String MENU_PATH = "menu";
	public final static String VERTICALE = "verticale";
	public final static String BASE_VERTICALE = "verticale";
	public final static String BASE_NIKONIKO = "nikoniko";

	public final static String USER = "/user";

	public final static String SHOW_GRAPH = "showgraph";
	public final static String SHOW_VERTICAL = "showverticale";
	public final static String SHOW_TEAM = "showTeam";
	public static final String SHOW_USERS = "showUser";
	public final static String SHOW_ROLE = "showRole";
	public final static String SHOW_NIKO = "showNiko";
	public final static String SHOW_NIKONIKO = "shownikonikos";
	public final static String SHOW_CHANGE_DATES = "showchangedates";
	public final static String SHOW_COMMENT = "showComment";
	public final static String SHOW_FUNC = "showFunction";

	public final static String SHOW_GRAPH_VERTICALE = "showgraphverticale";
	public final static String SHOW_GRAPH_TEAM = "showgraphteam";
	public final static String SHOW_GRAPH_ALL = "showgraphall";
	public final static String SHOW_GRAPH_MONTH = "showgraphmonth";
	public final static String SHOW_GRAPH_WEEK = "showgraphweek";
	public final static String SHOW_GRAPH_DATE = "showdate";

	public final static String GO_USERS = PATH + "user" + PATH;
	public final static String GO_TEAMS = PATH + "team" + PATH;
	public final static String GO_VERTICALE = PATH + "verticale" + PATH;
	public final static String GO_ROLES = PATH + "role" + PATH;
	public final static String GO_NIKOS =  PATH + "nikoniko" + PATH;
	public final static String GO_FUNCTIONS =  PATH + "function" + PATH;
	public final static String GO_GRAPHE = PATH + "graph" + PATH + "showgraphall";
	public final static String GO_CALENDAR = PATH + "graph" + PATH + "nikoniko" + PATH + "month";

	public final static String GO_USERTEAM = PATH + "user_has_team" + PATH;
	public final static String GO_USERROLE = PATH + "user_has_role" + PATH;
	public final static String GO_ROLEFUNC = PATH + "role_has_function" + PATH;

	public final static String ADD_FUNC = "addFunctions";
	public final static String ADD_USER = "addUsers";
	public final static String ADD_VERTICAL = "addVerticale";
	public final static String ADD_TEAM = "addTeams";
	public final static String ADD_ROLE = "addRoles";

	//From baseController
	public final static String LIST_ACTION= "list";
	public final static String LIST_ID_ACTION= "list_id";
	public final static String UPDATE_ACTION= "update";
	public final static String DELETE_ACTION= "delete";
	public final static String CREATE_ACTION= "create";
	public final static String SHOW_ACTION= "show";
	public final static String LOGIN_ACTION = "/login";

	public final static String PATH_LIST_FILE = PATH + LIST_ACTION ;
	public final static String PATH_LIST_ID_FILE = PATH + LIST_ID_ACTION ;
	public final static String PATH_UPDATE_FILE = PATH + UPDATE_ACTION ;
	public final static String PATH_DELETE_FILE = PATH + DELETE_ACTION ;
	public final static String PATH_CREATE_FILE = PATH + CREATE_ACTION ;
	public final static String PATH_SHOW_FILE = PATH + SHOW_ACTION ;
	public final static String PATH_LOGIN = PATH + LOGIN_ACTION;

	public final static String ROUTE_LIST = LIST_ACTION;
	public final static String ROUTE_UPDATE = "{id}/"+ UPDATE_ACTION;
	public final static String ROUTE_DELETE = "{id}/"+ DELETE_ACTION;
	public final static String ROUTE_CREATE = CREATE_ACTION;
	public final static String ROUTE_SHOW = "{id}/"+ SHOW_ACTION;

	public final static String ROUTE_LOGIN = LOGIN_ACTION;

}
