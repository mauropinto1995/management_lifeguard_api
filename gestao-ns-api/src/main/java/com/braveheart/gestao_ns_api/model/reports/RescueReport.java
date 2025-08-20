package com.braveheart.gestao_ns_api.model.reports;

import com.braveheart.gestao_ns_api.model.AbstractIDModel;
import com.braveheart.gestao_ns_api.model.Post;
import com.braveheart.gestao_ns_api.model.User;
import com.braveheart.gestao_ns_api.model.reports.enums.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"victim", "witnesses"})
@Entity
@Table(name = "rescue_reports")
public class RescueReport extends AbstractIDModel {

    @Column(name = "occurrence_timestamp", nullable = false)
    private Instant occurrenceTimestamp;

    @Column(name = "is_on_duty", nullable = false)
    private boolean isOnDuty;

    @Enumerated(EnumType.STRING)
    @Column(name = "incident_type", nullable = false)
    private IncidentType incidentType;

    @Column(name = "incident_type_other")
    private String incidentTypeOther;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", nullable = false)
    private InterventionOutcome outcome;

    @Enumerated(EnumType.STRING)
    @Column(name = "victim_activity", nullable = false)
    private VictimActivity victimActivity;

    @Column(name = "victim_activity_other")
    private String victimActivityOther;

    @Enumerated(EnumType.STRING)
    private WindCondition wind;

    @Enumerated(EnumType.STRING)
    private VisibilityCondition visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"current\"") // Aspas porque "current" pode ser uma palavra reservada
    private CurrentCondition current;

    @Enumerated(EnumType.STRING)
    private TideCondition tide;

    @Enumerated(EnumType.STRING)
    private WaveCondition waves;

    @Enumerated(EnumType.STRING)
    private FlagCondition flag;

    @Enumerated(EnumType.STRING)
    private EvacuationMethod evacuation;

    @Column(name = "incident_end_time")
    private Instant incidentEndTime;

    @Column(name = "additional_observations", columnDefinition = "TEXT")
    private String additionalObservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lifeguard_id", nullable = false)
    private User lifeguard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "report_equipment",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id"))
    private Set<Equipment> equipmentUsed = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "report_assisting_entities",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "entity_id"))
    private Set<AssistingEntity> assistingEntities = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "report_probable_causes",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "cause_id"))
    private Set<ProbableCause> probableCauses = new HashSet<>();


    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Victim victim;


    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Witness> witnesses = new ArrayList<>();
}