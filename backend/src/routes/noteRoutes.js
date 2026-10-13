import express from 'express'

import {
    getNotes,
    getNote,
    createNote,
    updateNote,
    deleteNote
} from '../controllers/noteController.js'

import { authenticateToken } from '../middleware/authMiddleware.js'

const router = express.Router()

router.get('/', authenticateToken, getNotes)

router.get('/:id', authenticateToken, getNote)

router.post('/', authenticateToken, createNote)

router.put('/:id', authenticateToken, updateNote)

router.delete('/:id', authenticateToken, deleteNote)

export default router