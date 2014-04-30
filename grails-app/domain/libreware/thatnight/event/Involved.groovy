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
    Car car
    int availableSeats
    String meetingPoint

    Role role

    static belongsTo = [user:User, event:Event ]

    static constraints = {
        user (nullable: false, blank:false)
        event (nullable: false, blank: false)
        car (nullable: true, blank: true)
        meetingPoint(nullable: true, blank: true)
        role(nullable : false, blank: false, validator:{
            value, obj ->
                if (role == Role.DRIVER) return (car != null)
                return true
        })
        availableSeats(validator:{
            value, obj ->
                return value < obj.car.capacity //ensure that the number of available seats is lesser than the car capacity
        })
        // TODO : Conditional constraint according to ROLE (Role.DRIVER must have a non null car etc.)
    }
}
