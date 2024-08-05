import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICard } from 'app/shared/model/card.model';
import { getEntities as getCards } from 'app/entities/card/card.reducer';
import { IStore } from 'app/shared/model/store.model';
import { getEntities as getStores } from 'app/entities/store/store.reducer';
import { IDeal } from 'app/shared/model/deal.model';
import { getEntity, updateEntity, createEntity, reset } from './deal.reducer';

export const DealUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cards = useAppSelector(state => state.card.entities);
  const stores = useAppSelector(state => state.store.entities);
  const dealEntity = useAppSelector(state => state.deal.entity);
  const loading = useAppSelector(state => state.deal.loading);
  const updating = useAppSelector(state => state.deal.updating);
  const updateSuccess = useAppSelector(state => state.deal.updateSuccess);

  const handleClose = () => {
    navigate('/deal');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCards({}));
    dispatch(getStores({}));
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
    if (values.discountPercentage !== undefined && typeof values.discountPercentage !== 'number') {
      values.discountPercentage = Number(values.discountPercentage);
    }
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    const entity = {
      ...dealEntity,
      ...values,
      card: cards.find(it => it.id.toString() === values.card?.toString()),
      store: stores.find(it => it.id.toString() === values.store?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          ...dealEntity,
          startDate: convertDateTimeFromServer(dealEntity.startDate),
          endDate: convertDateTimeFromServer(dealEntity.endDate),
          card: dealEntity?.card?.id,
          store: dealEntity?.store?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dealNdiscountApp.deal.home.createOrEditLabel" data-cy="DealCreateUpdateHeading">
            <Translate contentKey="dealNdiscountApp.deal.home.createOrEditLabel">Create or edit a Deal</Translate>
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
                  id="deal-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dealNdiscountApp.deal.discountPercentage')}
                id="deal-discountPercentage"
                name="discountPercentage"
                data-cy="discountPercentage"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 100, message: translate('entity.validation.max', { max: 100 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dealNdiscountApp.deal.description')}
                id="deal-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.deal.startDate')}
                id="deal-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.deal.endDate')}
                id="deal-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dealNdiscountApp.deal.isDealActive')}
                id="deal-isDealActive"
                name="isDealActive"
                data-cy="isDealActive"
                check
                type="checkbox"
              />
              <ValidatedField id="deal-card" name="card" data-cy="card" label={translate('dealNdiscountApp.deal.card')} type="select">
                <option value="" key="0" />
                {cards
                  ? cards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="deal-store" name="store" data-cy="store" label={translate('dealNdiscountApp.deal.store')} type="select">
                <option value="" key="0" />
                {stores
                  ? stores.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/deal" replace color="info">
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

export default DealUpdate;
