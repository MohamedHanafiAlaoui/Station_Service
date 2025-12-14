package com.example.station_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Admin extends User{
}
