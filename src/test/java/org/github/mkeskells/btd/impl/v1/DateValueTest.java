package org.github.mkeskells.btd.impl.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.*;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Random;

public class DateValueTest extends ValueTest {
  TemporalValue.LocalData writePrivateData;

  @BeforeEach
  void resetPrivateData() {
    writePrivateData = Value.DATE.generatePrivateData();
  }

  Writer<Temporal> writer = (data, value, scratch) -> Value.DATE.encode(data, value, scratch, writePrivateData);

  @Test
  void encodeLocalDate() throws IOException {
    encode(LocalDate.of(2560, 2, 3), writer, Value.DATE.start + TemporalValue.LOCAL_DATE, 0, 0, 10, 0, 2, 3);
  }

  @Test
  void roundTripLocalDate() throws IOException {
    roundTrip(Value.DATE, LocalDate.of(2000, 2, 3), LocalDate.class, writer);
    roundTrip(Value.DATE, LocalDate.of(2000, 2, 3), Temporal.class, writer);
    roundTrip(Value.DATE, LocalDate.of(2000, 2, 3), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalDate.of(2000, 2, 3), Object.class, writer);
  }

  @Test
  void roundTripRandomLocalDate() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      int year = r.nextInt();
      int month = r.nextInt(12) + 1;
      int day = r.nextInt(31) + 1;
      LocalDate test;
      try {
        test = LocalDate.of(year, month, day);
      } catch (DateTimeException e) {
        continue;
      }
      roundTrip(Value.DATE, test, LocalDate.class, writer);
    }
  }

  @Test
  void encodeLocalTime() throws IOException {
    encode(LocalTime.of(22, 2, 3), writer, Value.DATE.start + TemporalValue.LOCAL_TIME, 22, 2, 3, 255);
    encode(LocalTime.of(22, 2, 3, 0), writer, Value.DATE.start + TemporalValue.LOCAL_TIME, 22, 2, 3, 255);
    encode(LocalTime.of(22, 2, 3, 1), writer, Value.DATE.start + TemporalValue.LOCAL_TIME, 22, 2, 3, 0, 0, 0, 1);
    encode(LocalTime.of(22, 2, 3, 999_999_999), writer, Value.DATE.start + TemporalValue.LOCAL_TIME, 22, 2, 3, 0x3B, 0x9A, 0xC9, 0xFF);
  }

  @Test
  void roundTripLocalTime() throws IOException {
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3), LocalTime.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3), Temporal.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3), Object.class, writer);

    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 1), LocalTime.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 1), Temporal.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 1), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 1), Object.class, writer);

    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 999_999_999), LocalTime.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 999_999_999), Temporal.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 999_999_999), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalTime.of(22, 2, 3, 999_999_999), Object.class, writer);
  }

  @Test
  void roundTripRandomLocalTime() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      int hour = r.nextInt(24);
      int minute = r.nextInt(60);
      int seconds = r.nextInt(60);
      int nanos = r.nextInt(1_000_000_000);

      LocalTime test = LocalTime.of(hour, minute, seconds);
      roundTrip(Value.DATE, test, LocalTime.class, writer);

      test = LocalTime.of(hour, minute, seconds, nanos);
      roundTrip(Value.DATE, test, LocalTime.class, writer);
    }
  }

  @Test
  void encodeLocalDateTime() throws IOException {
    encode(LocalDateTime.of(2560, 5, 7, 22, 2, 3), writer, Value.DATE.start + TemporalValue.LOCAL_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255);
    encode(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 0), writer, Value.DATE.start + TemporalValue.LOCAL_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255);
    encode(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), writer, Value.DATE.start + TemporalValue.LOCAL_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0, 0, 0, 1);
    encode(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), writer, Value.DATE.start + TemporalValue.LOCAL_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0x3B, 0x9A, 0xC9, 0xFF);
  }

  @Test
  void roundTripLocalDateTime() throws IOException {
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3), LocalDateTime.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3), Temporal.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3), Object.class, writer);

    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), LocalDateTime.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), Temporal.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), Object.class, writer);

    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), LocalDateTime.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), Temporal.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), Object.class, writer);
  }

  @Test
  void roundTripRandomLocalDateTime() throws IOException {
    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      int year = r.nextInt();
      int month = r.nextInt(12) + 1;
      int day = r.nextInt(31) + 1;
      int hour = r.nextInt(24);
      int minute = r.nextInt(60);
      int seconds = r.nextInt(60);
      int nanos = r.nextInt(1_000_000_000);

      LocalDateTime test;
      try {
        test = LocalDateTime.of(year, month, day, hour, minute, seconds);
      } catch (DateTimeException e) {
        continue;
      }

      roundTrip(Value.DATE, test, LocalDateTime.class, writer);

      test = LocalDateTime.of(year, month, day, hour, minute, seconds, nanos);
      roundTrip(Value.DATE, test, LocalDateTime.class, writer);
    }
  }

  @Test
  void encodeInstant() throws IOException {
    encode(Instant.ofEpochSecond(0x02030405060708L), writer, Value.DATE.start + TemporalValue.INSTANT, 0, 2, 3, 4, 5, 6, 7, 8, 255);
    encode(Instant.ofEpochSecond(0x02030405060708L, 0), writer, Value.DATE.start + TemporalValue.INSTANT, 0, 2, 3, 4, 5, 6, 7, 8, 255);
    encode(Instant.ofEpochSecond(0x02030405060708L, 1), writer, Value.DATE.start + TemporalValue.INSTANT, 0, 2, 3, 4, 5, 6, 7, 8, 0, 0, 0, 1);
    encode(Instant.ofEpochSecond(0x02030405060708L, 999_999_999), writer, Value.DATE.start + TemporalValue.INSTANT, 0, 2, 3, 4, 5, 6, 7, 8, 0x3B, 0x9A, 0xC9, 0xFF);
  }

  @Test
  void roundTripInstant() throws IOException {
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L), Instant.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L), Temporal.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L), Object.class, writer);

    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 1), Instant.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 1), Temporal.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 1), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 1), Object.class, writer);

    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 999_999_999), Instant.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 999_999_999), Temporal.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 999_999_999), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, Instant.ofEpochSecond(0x02030405060708L, 999_999_999), Object.class, writer);
  }

  @Test
  void roundTripRandomInstant() throws IOException {
    //from Instant
    final long MIN_SECOND = -31557014167219200L;
    final long MAX_SECOND = 31556889864403199L;

    Random r = new Random();
    for (int i = 0; i < 10000; i++) {
      long seconds = r.nextLong();
      while (seconds < MIN_SECOND)
        seconds -= MIN_SECOND;
      while (seconds > MAX_SECOND)
        seconds -= MAX_SECOND;
      int nanos = r.nextInt(1_000_000_000);

      Instant test = Instant.ofEpochSecond(seconds);

      roundTrip(Value.DATE, test, Instant.class, writer);

      test = Instant.ofEpochSecond(seconds, nanos);
      roundTrip(Value.DATE, test, Instant.class, writer);
    }
  }

  @Test
  void encodeOffsetTime() throws IOException {
    //pre-registered
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 0);

    //register - id 1
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(0x1234)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0x12, 0x34);
    //reuse
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(0x1234)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 1);

    encode(OffsetTime.of(LocalTime.of(22, 2, 3, 0), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 0);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3, 1), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 0, 0, 0, 1, /* id */ 0);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3, 999_999_999), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 0x3B, 0x9A, 0xC9, 0xFF, /* id */ 0);

    //register - id 2 - 5
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(99)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0, 99);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(-99)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, TemporalValue.DECLARE_NOFFSET, 0, 99);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.MAX), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0xfd, 0x20);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.MIN), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, TemporalValue.DECLARE_NOFFSET, 0xfd, 0x20);
    //reuse
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(99)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 2);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.ofTotalSeconds(-99)), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 3);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.MIN), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 5);
    encode(OffsetTime.of(LocalTime.of(22, 2, 3), ZoneOffset.MAX), writer, Value.DATE.start + TemporalValue.OFFSET_TIME, 22, 2, 3, 255, /* id */ 4);

  }

  @Test
  void roundTripOffsetTime() throws IOException {
    roundTripOffsetTime(ZoneOffset.UTC);
    roundTripOffsetTime(ZoneOffset.MIN);
    roundTripOffsetTime(ZoneOffset.MAX);

    roundTripOffsetTime(ZoneOffset.ofTotalSeconds(3600));

  }

  void roundTripOffsetTime(ZoneOffset offset) throws IOException {


    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3), offset), OffsetTime.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3), offset), Object.class, writer);

    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 1), offset), OffsetTime.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 1), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 1), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 1), offset), Object.class, writer);

    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 999_999_999), offset), OffsetTime.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 999_999_999), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 999_999_999), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetTime.of(LocalTime.of(22, 2, 3, 999_999_999), offset), Object.class, writer);

  }

  @Test
  void roundTripRandomOffsetTime() throws IOException {
    int maxOffset = 18 * 3600;
    Random r = new Random();
    for (int i = 0; i < 100000; i++) {
      int hour = r.nextInt(24);
      int minute = r.nextInt(60);
      int seconds = r.nextInt(60);
      int nanos = r.nextInt(1_000_000_000);

      ZoneOffset offset = ZoneOffset.ofTotalSeconds((r.nextBoolean() ? 1 : -1) * r.nextInt(maxOffset));

      OffsetTime test = OffsetTime.of(LocalTime.of(hour, minute, seconds), ZoneOffset.UTC);
      roundTrip(Value.DATE, test, OffsetTime.class, writer);

      test = OffsetTime.of(LocalTime.of(hour, minute, seconds, nanos), offset);
      roundTrip(Value.DATE, test, OffsetTime.class, writer);
    }
  }

  @Test
  void encodeOffsetDateTime() throws IOException {
    //pre-registered
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /*  id*/ 0);

    //register - id 1
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(0x1234)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0x12, 0x34);
    //reuse
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(0x1234)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, 1);

    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 0), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, 0);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0, 0, 0, 1, 0);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0x3B, 0x9A, 0xC9, 0xFF, 0);

    //register - id 2 - 5
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(99)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0, 99);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(-99)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_NOFFSET, 0, 99);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.MAX), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0xfd, 0x20);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.MIN), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_NOFFSET, 0xfd, 0x20);
    //reuse
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(99)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* id */2);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.ofTotalSeconds(-99)), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* id */3);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.MIN), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* id */5);
    encode(OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.MAX), writer, Value.DATE.start + TemporalValue.OFFSET_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* id */4);

  }

  @Test
  void roundTripOffsetDateTime() throws IOException {
    roundTripOffsetDateTime(ZoneOffset.UTC);
    roundTripOffsetDateTime(ZoneOffset.MIN);
    roundTripOffsetDateTime(ZoneOffset.MAX);

    roundTripOffsetDateTime(ZoneOffset.ofTotalSeconds(3600));

  }

  void roundTripOffsetDateTime(ZoneOffset offset) throws IOException {


    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), OffsetDateTime.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), Object.class, writer);

    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), OffsetDateTime.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), Object.class, writer);

    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), OffsetDateTime.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), Temporal.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, OffsetDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), Object.class, writer);

  }

  @Test
  void roundTripRandomOffsetDateTime() throws IOException {
    int maxOffset = 18 * 3600;
    Random r = new Random();
    for (int i = 0; i < 100000; i++) {

      int year = r.nextInt();
      int month = r.nextInt(12) + 1;
      int day = r.nextInt(31) + 1;
      int hour = r.nextInt(24);
      int minute = r.nextInt(60);
      int seconds = r.nextInt(60);
      int nanos = r.nextInt(1_000_000_000);

      LocalDateTime ldt;
      try {
        ldt = LocalDateTime.of(year, month, day, hour, minute, seconds);
      } catch (DateTimeException e) {
        continue;
      }


      ZoneOffset offset = ZoneOffset.ofTotalSeconds((r.nextBoolean() ? 1 : -1) * r.nextInt(maxOffset));

      OffsetDateTime test = OffsetDateTime.of(ldt, ZoneOffset.UTC);
      roundTrip(Value.DATE, test, OffsetDateTime.class, writer);

      test = OffsetDateTime.of(LocalDateTime.of(year, month, day, hour, minute, seconds, nanos), offset);
      roundTrip(Value.DATE, test, OffsetDateTime.class, writer);
    }
  }

  @Test
  void encodeZonedDateTimeZoneIsOffset() throws IOException {
    //pre-registered - UTC - id 0
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* UTC */ 0, TemporalValue.OFFSET_IS_ZONE);
    //reuse
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* UTC */ 0, TemporalValue.OFFSET_IS_ZONE);

    ZoneOffset off1 = ZoneOffset.ofTotalSeconds(88);
    ZoneOffset off2 = ZoneOffset.ofTotalSeconds(0x1234);
    ZoneOffset noff1 = ZoneOffset.ofTotalSeconds(-0x88);
    //register - off1 = offset id 1
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), off1), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0, 88, TemporalValue.OFFSET_IS_ZONE);
    //reuse
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), off1), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* off1 */ 1, TemporalValue.OFFSET_IS_ZONE);

    //register - off2 = offset id 2
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), off2), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_POFFSET, 0x12, 0x34, TemporalValue.OFFSET_IS_ZONE);
    //reuse
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), off2), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* off2 */ 2, TemporalValue.OFFSET_IS_ZONE);

    //register - noff1 = offset id 3
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), noff1), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, TemporalValue.DECLARE_NOFFSET, 0, 0x88, TemporalValue.OFFSET_IS_ZONE);
    //reuse
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), noff1), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, /* noff1 */ 3, TemporalValue.OFFSET_IS_ZONE);

    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 0), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 255, 0, TemporalValue.OFFSET_IS_ZONE);
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0, 0, 0, 1, 0, TemporalValue.OFFSET_IS_ZONE);
    encode(ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), ZoneOffset.UTC), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 10, 0, 5, 7, 22, 2, 3, 0x3B, 0x9A, 0xC9, 0xFF, 0, TemporalValue.OFFSET_IS_ZONE);

  }

  @Test
  void encodeZonedDateTimeZoneIsNotOffset() throws IOException {

    //register - id 1
    encode(ZonedDateTime.ofLocal(LocalDateTime.of(2021, 10, 31, 1, 3, 4), ZoneId.of("Europe/London"), ZoneOffset.ofTotalSeconds(3600)), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 2021 / 256, 2021, 10, 31, 1, 3, 4, 255, TemporalValue.DECLARE_ZONE, StringValue.SHORT_BYTE_ENCODED + Value.STRING.start, 13, 'E', 'u', 'r', 'o', 'p', 'e', '/', 'L', 'o', 'n', 'd', 'o', 'n', TemporalValue.DECLARE_POFFSET, 3600 / 256, 3600);
    //reuse
    encode(ZonedDateTime.ofLocal(LocalDateTime.of(2021, 10, 31, 1, 3, 4), ZoneId.of("Europe/London"), ZoneOffset.ofTotalSeconds(3600)), writer, Value.DATE.start + TemporalValue.ZONED_DATETIME, 0, 0, 2021 / 256, 2021, 10, 31, 1, 3, 4, 255, /* id */ 1,  /* id */ 1);

  }

  @Test
  void roundTripZonedDateTime() throws IOException {
    roundTripZonedDateTime(ZoneOffset.UTC);
    roundTripZonedDateTime(ZoneOffset.MIN);
    roundTripZonedDateTime(ZoneOffset.MAX);

    roundTripZonedDateTime(ZoneOffset.ofTotalSeconds(3600));

  }

  void roundTripZonedDateTime(ZoneOffset offset) throws IOException {


    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), ZonedDateTime.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), Temporal.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3), offset), Object.class, writer);

    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), ZonedDateTime.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), Temporal.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 1), offset), Object.class, writer);

    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), ZonedDateTime.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), Temporal.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), TemporalAccessor.class, writer);
    roundTrip(Value.DATE, ZonedDateTime.of(LocalDateTime.of(2560, 5, 7, 22, 2, 3, 999_999_999), offset), Object.class, writer);

  }

  @Test
  void roundTripRandomZonedDateTime() throws IOException {
    int maxOffset = 18 * 3600;
    Random r = new Random();
    for (int i = 0; i < 100000; i++) {

      int year = r.nextInt();
      int month = r.nextInt(12) + 1;
      int day = r.nextInt(31) + 1;
      int hour = r.nextInt(24);
      int minute = r.nextInt(60);
      int seconds = r.nextInt(60);
      int nanos = r.nextInt(1_000_000_000);

      LocalDateTime ldt;
      try {
        ldt = LocalDateTime.of(year, month, day, hour, minute, seconds);
      } catch (DateTimeException e) {
        continue;
      }


      ZoneOffset offset = ZoneOffset.ofTotalSeconds((r.nextBoolean() ? 1 : -1) * r.nextInt(maxOffset));

      ZonedDateTime test = ZonedDateTime.of(ldt, ZoneOffset.UTC);
      roundTrip(Value.DATE, test, ZonedDateTime.class, writer);

      test = ZonedDateTime.of(LocalDateTime.of(year, month, day, hour, minute, seconds, nanos), offset);
      roundTrip(Value.DATE, test, ZonedDateTime.class, writer);
    }
  }

}
