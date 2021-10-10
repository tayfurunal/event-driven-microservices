package com.tayfurunal.basketservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "basket")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "status_id", nullable = false)
    private Integer statusId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id",
            foreignKey = @ForeignKey(name = "FK__basket__basket_status"), insertable = false, updatable = false)
    private BasketStatus basketStatus;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK__basket__basket_product"))
    private List<BasketProduct> basketProducts = new ArrayList<>();
}
