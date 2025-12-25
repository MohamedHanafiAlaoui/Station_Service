package com.example.station_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Admin extends User{
}
