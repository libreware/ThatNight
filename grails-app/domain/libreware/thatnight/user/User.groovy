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

import libreware.thatnight.event.Event
import libreware.thatnight.event.Involved

class User {
    /**
     * User login name, the one that's shown to everybody
     */
    String loginName

    /**
     * First name of the user
     */
    String firstName

    /**
     * Last name of the user
     */
    String lastName

    /**
     * User's email address
     */
    String email

    /**
     * User password (hashed)
     */
    String hashedPassword

    /**
     * The password before it is hashed (is not stored in the database, just used fot the validator)
     */
    String password

    /**
     * Confirm password
     */
    String confirmPassword

    static hasMany = [cars:Car, involvements:Involved]

    static transients = ['password','confirmPassword']

    static constraints = {
        loginName(size: 2..15, blank: false, matches: "[a-zA-Z0-9][a-zA-Z0-9 ]+", unique: true)
        firstName(size: 2..20, blank: false, matches: "[a-zA-Z][a-zA-Z -]+")
        lastName(size: 2..20, blank: false, matches: "[a-zA-Z][a-zA-Z -]+")
        email(email:true, nullable:false, blank: false, unique: true)
        hashedPassword(nullable:false, blank: false)
        password  (blank:false, size:5..15, matches:/[\S]+/, validator:{ val, obj ->
            if (obj.password != obj.confirmPassword)
                return 'user.password.match.error'
        })
    }
}
