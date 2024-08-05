import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './deal.reducer';

export const DealDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dealEntity = useAppSelector(state => state.deal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dealDetailsHeading">
          <Translate contentKey="dealNdiscountApp.deal.detail.title">Deal</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dealEntity.id}</dd>
          <dt>
            <span id="discountPercentage">
              <Translate contentKey="dealNdiscountApp.deal.discountPercentage">Discount Percentage</Translate>
            </span>
          </dt>
          <dd>{dealEntity.discountPercentage}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dealNdiscountApp.deal.description">Description</Translate>
            </span>
          </dt>
          <dd>{dealEntity.description}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="dealNdiscountApp.deal.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>{dealEntity.startDate ? <TextFormat value={dealEntity.startDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="dealNdiscountApp.deal.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{dealEntity.endDate ? <TextFormat value={dealEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isDealActive">
              <Translate contentKey="dealNdiscountApp.deal.isDealActive">Is Deal Active</Translate>
            </span>
          </dt>
          <dd>{dealEntity.isDealActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="dealNdiscountApp.deal.card">Card</Translate>
          </dt>
          <dd>{dealEntity.card ? dealEntity.card.id : ''}</dd>
          <dt>
            <Translate contentKey="dealNdiscountApp.deal.store">Store</Translate>
          </dt>
          <dd>{dealEntity.store ? dealEntity.store.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/deal" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/deal/${dealEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DealDetail;
