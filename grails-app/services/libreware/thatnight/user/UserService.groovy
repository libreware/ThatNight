/*
ThatNight is a social networking application based on the event creation.
It links carpooling, hosting, and event programming.
Copyright (C) 2014  libreware (https://github.com/libreware)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package libreware.thatnight.user

import grails.transaction.Transactional
import libreware.simplesec.SessionService
import libreware.thatnight.UnauthorizedOperationException
import libreware.thatnight.UserNotInvolvedException
import libreware.thatnight.event.Event
import libreware.thatnight.event.Involved
import libreware.thatnight.event.Role
import org.apache.commons.logging.LogFactory

@Transactional
class UserService {
    /**
     * Logger
     */
    private static final log = LogFactory.getLog('grails.app.' + UserService.class.name)

    SessionService sessionService;

    /**
     * Saves a user and saves it in the database. The user is immediately persisted
     * so it can be used immediately for logging in the app.
     *
     * @param user - the user to save
     * @return true if success false if process failed
     */
    public static boolean save(User user){
        if (user.save(flush: true)) { // call saving service
            log.info("New user created: "+user)
            return true
        } else {
            log.warn("User creation failed: "+user)
            return false
        }
    }

    /**
     * Saves the edited user in the database.
     *
     * @param user - the edited user
     * @return true if success false if process failed
     */
    public static boolean edit(User user){
        user = user.merge(flush: true) // explanations below
        // merge is similar in function to the save
        // for details on merge function
        // @see http://grails.org/doc/2.2.1/ref/Domain%20Classes/merge.html

        if (user) { // call saving service
            log.info("User edited: "+user)
            return true
        } else {
            log.warn("User edit failed: "+user)
            return false
        }
    }

    /**
     * Deletes a user and all his involved objects, his events and his cars
     *
     * @param user the user to remove
     */
    public static void delete(User user){
        user.delete(flush: true)
        log.info("User edited: "+user)
    }

    /**
     * Involves the current user in an event and gives him the specified role
     *
     * @param event the event where the user will be involved
     * @param role the role that he'll get
     */
    public involve(Event event, Role role){
        User user = sessionService.getUser()
        Involved involvement = new Involved(user: user, event: event)
        involvement.addRole(role)
        user.addToInvolvements(involvement)
    }

    /**
     * Removes the involvement of the current user in an event
     *
     * @param event the event where the user's involvement will be removed
     */
    public uninvolve(Event event){
        User user = sessionService.getUser()
        Involved involvement = Involved.find(new Involved(user: user, event: event))
        involvement.delete()
    }

    /**
     * Adds a role for the current user on his involvement
     *
     * @param event the event where to add the role
     * @param role the role to add
     * @return false if this role is already given to the user or not authorized
     */
    public boolean addRoleToEvent(Event event, Role role) throws UnauthorizedOperationException, UserNotInvolvedException{
        // TODO externalize error strings
        if (role == Role.OWNER) throw new UnauthorizedOperationException("The role owner can't be self attributed.")
        User user = sessionService.getUser()
        Involved involvement = Involved.find(new Involved(user: user, event: event))
        if (involvement == null) throw new UserNotInvolvedException("The user "+user+" trying to add a role on the event "+event+" is not involved in it.")
        if (!involvement.getRole().contains(role)){
            involvement.addRole(role)
            involvement.save()
            return true
        } else return false // user already has this role
    }

    /**
     * Should only be used to add the driver role to the user's involvement
     *
     * @param event
     * @param role
     * @param car
     * @param meetingPoint
     * @param availableSeats
     */
    public addRoleToEvent(Event event, Role role, Car car, String meetingPoint, int availableSeats){
        // TODO externalize error strings
        if (role == Role.OWNER) throw new UnauthorizedOperationException("The role owner can't be self attributed.")
        User user = sessionService.getUser()
        Involved involvement = Involved.find(new Involved(user: user, event: event))
        if (involvement == null) throw new UserNotInvolvedException("The user "+user+" trying to add a role on the event "+event+" is not involved in it.")
        if (!involvement.getRole().contains(role)){
            involvement.addRole(role) // checking that car and available seats are there when role is not done here
            involvement.meetingPoint = meetingPoint
            involvement.availableSeats = availableSeats
            involvement.car = car
            involvement.save()
            return true
        } else return false // user already has this role
    }

    /**
     *
     * @param event
     * @param role
     * @return
     */
    public boolean removeRoleFromEvent(Event event,  Role role){
        User user = sessionService.getUser()
        Involved involvement = Involved.find(new Involved(user: user, event: event))
        if (role == Role.OWNER){
            //TODO check that there is an other owner if the owner is being removed

        }
        if (involvement == null) throw new UserNotInvolvedException("The user "+user+" trying to remove a role on the event "+event+" is not involved in it.")
        if (!involvement.getRole().contains(role)) throw new UserNotInvolvedException("The user "+user+" trying to remove a role doesn't have that role. ")
        involvement.removeRole(role)
        return involvement.save()
    }
}

