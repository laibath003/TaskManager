import app from './app.js'
import pool from './db/db.js'

const PORT = process.env.PORT || 8000

app.listen(PORT, '0.0.0.0', async () => {
    console.log(`Server running on http://localhost:${PORT}`)

    try {
        const result = await pool.query('SELECT NOW()')
        console.log('PostgreSQL connected:', result.rows[0])
    } catch (error) {
        console.error('PostgreSQL connection failed:', error.message)
    }
})