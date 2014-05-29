package libreware.thatnight.common

import grails.transaction.Transactional

class GeneralService {
    /**
     * Returns current time
     * @return current time
     */
    def static Date getTime(){
        TimeZone reference = TimeZone.getTimeZone("GMT")
        Calendar myCal = Calendar.getInstance(reference)
        TimeZone.setDefault(reference);
        return myCal.getTime()
    }

}
