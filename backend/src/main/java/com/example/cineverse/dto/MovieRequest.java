// src/main/java/com/example/cineverse/dto/MovieRequest.java
package com.example.cineverse.dto;

import java.sql.Date;

public class MovieRequest {
    public String title;
    public Date release_date;   // ISO-8601 “yyyy-MM-dd” is default
    public String posterUrl;
    public String rated;
    public String director;
    public String actors;
    public String plot;
}
