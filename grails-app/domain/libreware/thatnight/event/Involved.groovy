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

package libreware.thatnight.event

import libreware.thatnight.user.Car
import libreware.thatnight.user.User

class Involved {
    /**
     * The car the user uses for the event if he uses one.
     */
    Car car

    /**
     * The available seats
     */
    int availableSeats

    /**
     * The meeting point for the car departure
     */
    String meetingPoint

    /**
     * Roles of the user in the event
     */
    Collection<Role> role = new ArrayList<Role>()

    static belongsTo = [user:User, event:Event ]

    static constraints = {
        user (nullable: false, blank:false)
        event (nullable: false, blank: false)
        car (nullable: false, blank: true)
        meetingPoint(nullable: false, blank: true)
        role(nullable : false, blank: false, validator:{
            role, obj ->
                if (role.contains(Role.DRIVER)) return (car != null)
                return true
        })
        availableSeats(nullable:false, blank: true, validator:{
            value, obj ->
                return value < obj.car.capacity //ensure that the number of available seats is lesser than the car capacity
        })
    }

    public addRole(Role role){
        this.role.add(role)
    }

    public removeRole(Role role){
        this.role.remove(role)
    }
}
