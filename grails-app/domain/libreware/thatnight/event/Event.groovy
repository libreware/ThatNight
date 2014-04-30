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

class Event {

    /**
     * The name of the event
     */
    String title
    /**
     * The address where it takes place
     */
    String address
    /**
     * The date when the event begins
     */
    Date dateBeginEvent
    /**
     * The ending date of the event
     */
    Date dateEndEvent
    /**
     * Small text describing the event
     */
    String description
    /**
     * Type of the event: Private, public ...
     */
    Type privacy

    static hasMany = [involved:Involved]

    static constraints = {
        title(size:3..50, blank:false, nullable:false, matches: "^[a-zA-Z][a-zA-Z ]+", unique: true)
        address(size: 2..100, blank: false,nullable:false, matches: "^[a-zA-Z0-9][a-zA-Z0-9 ]+")
        dateBeginEvent(nullable:false, min: new Date())
        description(size: 2..300, blank: false, matches: "^[a-zA-Z0-9][a-zA-Z0-9 ]+")
        privacy(nullable: false, blank: false)
        dateEndEvent(nullable:false, min: new Date(), validator:{
            value, obj ->
                return value.after(obj.dateBeginEvent) // ensure that the event end after it begins
        })
    }
}
