import pool from '../db/db.js'

export const getNotes = async (req, res) => {
    try {
        const userId = req.user.userId
        const { search } = req.query

        let query = `
            SELECT id, title, content, created_at
            FROM notes
            WHERE user_id = $1
        `

        const params = [userId]

        if (search) {
            query += `
                AND (
                    title ILIKE $2
                    OR content ILIKE $2
                )
            `

            params.push(`%${search}%`)
        }

        query += ` ORDER BY created_at DESC`

        const result = await pool.query(query, params)

        res.json(result.rows)

    } catch (error) {
        console.error(error)

        res.status(500).json({
            message: 'Server error'
        })
    }
}

export const getNote = async (req, res) => {
    try {
        const userId = req.user.userId
        const noteId = req.params.id

        const result = await pool.query(
            `SELECT id, title, content, created_at
             FROM notes
             WHERE id = $1 AND user_id = $2`,
            [noteId, userId]
        )

        if (result.rows.length === 0) {
            return res.status(404).json({
                message: 'Note not found'
            })
        }

        res.json(result.rows[0])

    } catch (error) {
        console.error(error)

        res.status(500).json({
            message: 'Server error'
        })
    }
}


export const createNote = async (req, res) => {
    try {
        const userId = req.user.userId
        const { title, content } = req.body

        if (
            typeof title !== 'string' ||
            typeof content !== 'string' ||
            !title.trim() ||
            !content.trim()
        ) {
            return res.status(400).json({
                message: 'Title and content are required'
            })
        }

        const result = await pool.query(
            `INSERT INTO notes (user_id, title, content)
             VALUES ($1, $2, $3)
             RETURNING id, title, content, created_at`,
            [userId, title, content]
        )

        res.status(201).json(result.rows[0])

    } catch (error) {
        console.error(error)

        res.status(500).json({
            message: 'Server error'
        })
    }
}


export const updateNote = async (req, res) => {
    try {
        const userId = req.user.userId
        const noteId = req.params.id
        const { title, content } = req.body

        if (
            typeof title !== 'string' ||
            typeof content !== 'string' ||
            !title.trim() ||
            !content.trim()
        ) {
            return res.status(400).json({
                message: 'Title and content are required'
            })
        }

        const result = await pool.query(
            `UPDATE notes
             SET title = $1, content = $2
             WHERE id = $3 AND user_id = $4
             RETURNING id, title, content, created_at`,
            [title, content, noteId, userId]
        )

        if (result.rows.length === 0) {
            return res.status(404).json({
                message: 'Note not found'
            })
        }

        res.json(result.rows[0])

    } catch (error) {
        console.error(error)

        res.status(500).json({
            message: 'Server error'
        })
    }
}

export const deleteNote = async (req, res) => {
    try {
        const userId = req.user.userId
        const noteId = req.params.id

        const result = await pool.query(
            `DELETE FROM notes
             WHERE id = $1 AND user_id = $2
             RETURNING id`,
            [noteId, userId]
        )

        if (result.rows.length === 0) {
            return res.status(404).json({
                message: 'Note not found'
            })
        }

        res.json({
            message: 'Note deleted successfully'
        })

    } catch (error) {
        console.error(error)

        res.status(500).json({
            message: 'Server error'
        })
    }
}