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

/**
 * Created by Ben on 25/01/14.
 */

package libreware.thatnight.event;

public enum Type {
    PUBLIC("Public"), PRIVATE("Private");

    final String value;

    Type(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public String getKey() {
        return name();
    }
};
