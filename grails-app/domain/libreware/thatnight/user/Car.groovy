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

class Car {

    String name
    int capacity // include driver and passengers
    static belongsTo = [user: User]


    static constraints = {
        user(blank: false, nullable: false)
        name(blank: false, nullable: false)
        capacity(range: 2..7, blank: false, nullable: false)
    }
}
