package id.ac.ui.cs.advprog.beachievement.model;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ClanPromotedEvent {
  private String eventId;
  private String clanId;
  private String clanName;
  private String tier;
  private List<UUID> userIds;
}
