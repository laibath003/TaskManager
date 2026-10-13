import express from 'express'
import cors from 'cors'
import dotenv from 'dotenv'

import authRoutes from './routes/authRoutes.js'
import noteRoutes from './routes/noteRoutes.js'
import { errorHandler } from './middleware/errorMiddleware.js'

dotenv.config()

const app = express()

app.use(cors())
app.use(express.json())

app.get('/api', (req, res) => {
    res.json({
        message: 'Notes API is running'
    })
})

app.use('/api/auth', authRoutes)
app.use('/api/notes', noteRoutes)

app.use(errorHandler)

export default app