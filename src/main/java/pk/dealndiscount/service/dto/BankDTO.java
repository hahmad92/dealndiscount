package pk.dealndiscount.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pk.dealndiscount.domain.enumeration.BankType;

/**
 * A DTO for the {@link pk.dealndiscount.domain.Bank} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankDTO implements Serializable {

    private Long id;

    @NotNull
    private String bankName;

    @NotNull
    private BankType bankType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BankType getBankType() {
        return bankType;
    }

    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankDTO)) {
            return false;
        }

        BankDTO bankDTO = (BankDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + getId() +
            ", bankName='" + getBankName() + "'" +
            ", bankType='" + getBankType() + "'" +
            "}";
    }
}
