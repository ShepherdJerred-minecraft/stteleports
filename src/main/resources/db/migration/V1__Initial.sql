CREATE TABLE players (
  player_uuid                  CHAR(36) PRIMARY KEY,
  max_homes                    INT    NOT NULL,
  cooldown                     LONG   NOT NULL,
  cooldown_multiplier          DOUBLE NOT NULL,
  cost_multiplier              DOUBLE NOT NULL,
  cooldown_multiplier_modifier DOUBLE NOT NULL,
  cost_multiplier_modifier     DOUBLE NOT NULL
);

CREATE TABLE player_homes (
  player_uuid CHAR(36)    NOT NULL,
  name        VARCHAR(16) NOT NULL,
  world       CHAR(36)    NOT NULL,
  x           INT         NOT NULL,
  y           INT         NOT NULL,
  z           INT         NOT NULL,
  yaw         FLOAT       NOT NULL,
  pitch       FLOAT       NOT NULL,
  CONSTRAINT pk_player_home PRIMARY KEY (player_uuid, name),
  CONSTRAINT fk_player_uuid FOREIGN KEY (player_uuid) REFERENCES players (player_uuid)
    ON DELETE CASCADE
);