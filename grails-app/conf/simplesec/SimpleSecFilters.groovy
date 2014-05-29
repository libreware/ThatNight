package simplesec

import libreware.simplesec.SessionService
import libreware.simplesec.SimpleSecService

class SimpleSecFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                def securityService = new SimpleSecService()
                def sessionService = new SessionService()

                if (!actionName.equals('login') && !actionName.equals('create') && !request.forwardURI.equals("/StageTime/")) {
                    if (!securityService.isUserLoggedIn(sessionService.getUser())){
                        sessionService.setWaypoint(controllerName,actionName)
                        flash.message = "sec.login.first"
                        redirect(controller: 'user', action: 'login')
                        return false
                    }
                }
                return true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}