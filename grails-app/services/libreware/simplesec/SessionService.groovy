package libreware.simplesec

import groovy.time.TimeCategory
import libreware.thatnight.common.GeneralService
import libreware.thatnight.user.User
import org.springframework.beans.factory.InitializingBean

class SessionService implements InitializingBean{
    /**
     * Session is created only once
     */
    static scope = 'session'

    /**
     * Time to wait after MAX_LOGIN_ATTEMPTS login attempts
     */
    public static int WAITING_TIME

    /**
     * The maximum login fail before waiting for WAITING_TIME
     */
    public static int MAX_LOGIN_ATTEMPTS

    private String waypointController
    private String waypointAction

    private User user

    int numberOfConnections
    Date lastConnectionTime
    Date nextPossibility

    def grailsApplication

    @Override
    void afterPropertiesSet() throws Exception {
        // loading configuration
        MAX_LOGIN_ATTEMPTS = grailsApplication.config.max.login.attempts
        WAITING_TIME = grailsApplication.config.waiting.time
        numberOfConnections = 0
        use (TimeCategory) {
            nextPossibility = GeneralService.getTime() - 1.minutes
        }
        waypointController = null
        waypointAction = null
        user = null
    }

    /**
     * Redirection method, sends the path to redirect to as a string
     * @return the redirection string
     */
    public String getWaypoint(){
        String controller = this.waypointController
        String action = this.waypointAction
        this.waypointController = null
        this.waypointAction = null

        if (!controller || !action) {
            return "/"
        }else{
            return "/"+controller+"/"+action+"/"
        }
    }

    public String setWaypoint(String controller, String action){
        this.waypointController = controller
        this.waypointAction = action
    }

    public getUser(){
        return this.user;
    }

    public setUser(user){
        this.user = user
    }

    /**
     * Record a new attempt
     */
    protected void recordAttempt(){
        lastConnectionTime = GeneralService.getTime()
        numberOfConnections ++
        if(numberOfConnections >= MAX_LOGIN_ATTEMPTS){
            use (TimeCategory) {
                nextPossibility = GeneralService.getTime() + WAITING_TIME.minutes
            }
            numberOfConnections = 0
        }
    }

    protected void eraseAttempts(){
        use (TimeCategory) {
            nextPossibility = GeneralService.getTime() - 1.minutes
        }
        numberOfConnections = 0
    }

    protected void clean(){
        eraseAttempts()
        user=null
        waypointController=null
        waypointAction=null
    }
}
