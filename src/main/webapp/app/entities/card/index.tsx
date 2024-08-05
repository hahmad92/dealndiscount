import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Card from './card';
import CardDetail from './card-detail';
import CardUpdate from './card-update';
import CardDeleteDialog from './card-delete-dialog';

const CardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Card />} />
    <Route path="new" element={<CardUpdate />} />
    <Route path=":id">
      <Route index element={<CardDetail />} />
      <Route path="edit" element={<CardUpdate />} />
      <Route path="delete" element={<CardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CardRoutes;
