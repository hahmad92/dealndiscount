import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBank } from 'app/shared/model/bank.model';
import { getEntities as getBanks } from 'app/entities/bank/bank.reducer';
import { ICard } from 'app/shared/model/card.model';
import { CardCategory } from 'app/shared/model/enumerations/card-category.model';
import { CardNetwork } from 'app/shared/model/enumerations/card-network.model';
import { getEntity, updateEntity, createEntity, reset } from './card.reducer';

export const CardUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const banks = useAppSelector(state => state.bank.entities);
  const cardEntity = useAppSelector(state => state.card.entity);
  const loading = useAppSelector(state => state.card.loading);
  const updating = useAppSelector(state => state.card.updating);
  const updateSuccess = useAppSelector(state => state.card.updateSuccess);
  const cardCategoryValues = Object.keys(CardCategory);
  const cardNetworkValues = Object.keys(CardNetwork);

  const handleClose = () => {
    navigate('/card' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBanks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...cardEntity,
      ...values,
      bank: banks.find(it => it.id.toString() === values.bank?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          category: 'CREDIT',
          cardNetwork: 'VISA',
          ...cardEntity,
          bank: cardEntity?.bank?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dealNdiscountApp.card.home.createOrEditLabel" data-cy="CardCreateUpdateHeading">
            <Translate contentKey="dealNdiscountApp.card.home.createOrEditLabel">Create or edit a Card</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="card-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dealNdiscountApp.card.cardName')}
                id="card-cardName"
                name="cardName"
                data-cy="cardName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.card.category')}
                id="card-category"
                name="category"
                data-cy="category"
                type="select"
              >
                {cardCategoryValues.map(cardCategory => (
                  <option value={cardCategory} key={cardCategory}>
                    {translate('dealNdiscountApp.CardCategory.' + cardCategory)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('dealNdiscountApp.card.cardNetwork')}
                id="card-cardNetwork"
                name="cardNetwork"
                data-cy="cardNetwork"
                type="select"
              >
                {cardNetworkValues.map(cardNetwork => (
                  <option value={cardNetwork} key={cardNetwork}>
                    {translate('dealNdiscountApp.CardNetwork.' + cardNetwork)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="card-bank" name="bank" data-cy="bank" label={translate('dealNdiscountApp.card.bank')} type="select">
                <option value="" key="0" />
                {banks
                  ? banks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CardUpdate;
