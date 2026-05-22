package id.ac.ui.cs.advprog.beachievement.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClanPromotedEventTests {

  @Test
  void testGettersAndSetters() {
    ClanPromotedEvent event = new ClanPromotedEvent();
    UUID user = UUID.fromString("2a7d6f27-e090-4b2d-b950-d977aaad8d15");
    
    event.setEventId("event-123");
    event.setClanId("clan-456");
    event.setClanName("Diamond Force");
    event.setTier("Diamond");
    event.setUserIds(List.of(user));

    assertEquals("event-123", event.getEventId());
    assertEquals("clan-456", event.getClanId());
    assertEquals("Diamond Force", event.getClanName());
    assertEquals("Diamond", event.getTier());
    assertEquals(1, event.getUserIds().size());
    assertEquals(user, event.getUserIds().get(0));
  }

  @Test
  void testEqualsAndHashCode() {
    ClanPromotedEvent event1 = new ClanPromotedEvent();
    ClanPromotedEvent event2 = new ClanPromotedEvent();

    event1.setEventId("id");
    event2.setEventId("id");

    assertEquals(event1, event2);
    assertEquals(event1.hashCode(), event2.hashCode());
  }
}
