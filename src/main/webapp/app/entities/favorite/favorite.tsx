import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './favorite.reducer';

export const Favorite = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const favoriteList = useAppSelector(state => state.favorite.entities);
  const loading = useAppSelector(state => state.favorite.loading);
  const totalItems = useAppSelector(state => state.favorite.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="favorite-heading" data-cy="FavoriteHeading">
        <Translate contentKey="dealNdiscountApp.favorite.home.title">Favorites</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dealNdiscountApp.favorite.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/favorite/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dealNdiscountApp.favorite.home.createLabel">Create new Favorite</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {favoriteList && favoriteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dealNdiscountApp.favorite.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dealNdiscountApp.favorite.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('storeId')}>
                  <Translate contentKey="dealNdiscountApp.favorite.storeId">Store Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storeId')} />
                </th>
                <th className="hand" onClick={sort('cityId')}>
                  <Translate contentKey="dealNdiscountApp.favorite.cityId">City Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cityId')} />
                </th>
                <th className="hand" onClick={sort('cardId')}>
                  <Translate contentKey="dealNdiscountApp.favorite.cardId">Card Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cardId')} />
                </th>
                <th className="hand" onClick={sort('favoriteType')}>
                  <Translate contentKey="dealNdiscountApp.favorite.favoriteType">Favorite Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('favoriteType')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {favoriteList.map((favorite, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/favorite/${favorite.id}`} color="link" size="sm">
                      {favorite.id}
                    </Button>
                  </td>
                  <td>{favorite.userId}</td>
                  <td>{favorite.storeId}</td>
                  <td>{favorite.cityId}</td>
                  <td>{favorite.cardId}</td>
                  <td>
                    <Translate contentKey={`dealNdiscountApp.FavoriteType.${favorite.favoriteType}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/favorite/${favorite.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/favorite/${favorite.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/favorite/${favorite.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dealNdiscountApp.favorite.home.notFound">No Favorites found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={favoriteList && favoriteList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Favorite;
