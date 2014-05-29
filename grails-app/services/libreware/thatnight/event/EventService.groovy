package libreware.thatnight.event

import grails.transaction.Transactional
import libreware.simplesec.SessionService
import org.apache.commons.logging.LogFactory

@Transactional
class EventService {
    SessionService sessionService

    private static final log = LogFactory.getLog('grails.app.' + EventService.class.name)

    /**
     * Creates an event, saves it and create the user's involvement
     * to the event as it owner. It will take the user that is in the
     * session attribute.
     *
     * @param event - the event to save
     * @return true if the creation was sucessfull
     */
    public boolean save(Event event){
        if (!event.save(flush: true)) {
            log.warn("Event creation failed: "+event)
            return false
        } else {
            Involved involvement = new Involved(event:event, user:sessionService.getUser())
            involvement.addRole(new Role(Role.OWNER))
            event.addToInvolved(involvement)
            log.info("New event created: "+event)
            return true
        }
    }

    /**
     * Deletes an event and all his involved objects
     *
     * @param event the id of the event to remove
     */
    public boolean delete(Event event){
        event.delete(flush: true)
        log.info("Event deleted: "+event)
    }

    /**
     * Saves the edited user in the database.
     *
     * @param event - the edited user
     * @return true if success false if process failed
     */
    public boolean edit(Event event){
        event = event.merge(flush: true) // explanations below
        // merge is similar in function to the save
        // for details on merge function
        // @see http://grails.org/doc/2.2.1/ref/Domain%20Classes/merge.html

        if (event) { // call saving service
            log.info("Event edited: "+event)
            return true
        } else {
            log.warn("Event edit failed: "+event)
            return false
        }
    }

    /**
     * Returns the event matching with the given id
     *
     * @param eventId - the event id
     * @return the event
     */
    public Event getEvent(eventId){
        return Event.find { id == eventId }
    }
}
